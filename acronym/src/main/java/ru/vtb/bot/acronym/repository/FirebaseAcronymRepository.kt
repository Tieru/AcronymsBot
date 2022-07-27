package ru.vtb.bot.acronym.repository

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vtb.bot.acronym.entity.AcronymData
import ru.vtb.bot.acronym.entity.AppException
import ru.vtb.bot.acronym.entity.UserData
import ru.vtb.bot.acronym.ext.clearMarkdownEscaping
import java.util.concurrent.locks.ReentrantLock

class FirebaseAcronymRepository(
    private val firestore: Firestore,
) : AcronymRepository {
    private var cachedData: List<AcronymData>? = null
    private var lock = ReentrantLock()

    override fun getAcronyms(): List<AcronymData> {
        return cachedData ?: throw AppException("Не удалось загрузить данные. Попробуйте позже")
    }

    override suspend fun reloadData() {
        cachedData = loadData()
    }

    private suspend fun loadData(): List<AcronymData> {
        return withContext(Dispatchers.IO) {
            val collection = firestore.collection(COLLECTION).get()
            val collectionSnapshot = collection.get()
            collectionSnapshot.documents.map(::documentToAcronym)
        }
    }

    private fun documentToAcronym(document: QueryDocumentSnapshot): AcronymData {
        val (username, userId) = document.getString("addedBy").parseUserId()
        return AcronymData(
            id = document.id,
            value = document.getString("value") ?: "",
            description = document.getString("description") ?: "",
            addedById = userId,
            addedByUsername = username,
        )
    }

    override suspend fun addAcronym(acronym: String, description: String, userId: Long, username: String?): AcronymData {
        return withContext(Dispatchers.IO) {
            val clearDescription = description.clearMarkdownEscaping()
            val values = mapOf(
                "value" to acronym,
                "description" to clearDescription,
                "addedBy" to UserData.formatUsernameAndId(username = username.orEmpty(), id = userId),
            )

            firestore.collection(COLLECTION).document(acronym).set(values).get()

            val acronymData = AcronymData(
                id = acronym,
                value = acronym,
                description = clearDescription,
                addedById = userId,
                addedByUsername = username,
            )

            lock.lock()
            val storedData = cachedData ?: throw AppException("Ошибка. Данные не были загружены")
            val storedAcronym = storedData.find { it.value == acronym }
            cachedData = if (storedAcronym == null) {
                storedData.plus(acronymData)
            } else {
                storedData.map { if (it.value == acronym) acronymData else it }
            }
            lock.unlock()

            acronymData
        }
    }

    override suspend fun removeAcronym(acronym: AcronymData) {
        lock.lock()
        withContext(Dispatchers.IO) {
            firestore.collection(COLLECTION).document(acronym.value).delete().get()
            cachedData = cachedData?.filter { it.id != acronym.id }
        }
        lock.unlock()
    }

    private fun String?.parseUserId(): Pair<String?, Long?> {
        if (this == null) {
            return Pair(null, null)
        }
        val values = split(" / ")
        if (values.size < 2) {
            return Pair(null, null)
        }
        return values[0] to values[1].toLongOrNull()
    }

    companion object {
        const val COLLECTION = "acronyms"
    }
}
