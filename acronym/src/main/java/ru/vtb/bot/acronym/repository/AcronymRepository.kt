package ru.vtb.bot.acronym.repository

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vtb.bot.acronym.entity.AcronymData
import ru.vtb.bot.acronym.entity.AppException
import java.util.concurrent.locks.ReentrantLock

class AcronymRepository(
    private val firestore: Firestore,
) {
    private var cachedData: List<AcronymData>? = null
    private var lock = ReentrantLock()

    fun getAcronyms(): List<AcronymData> {
        return cachedData ?: throw AppException("Не удалось загрузить данные. Попробуйте позже")
    }

    suspend fun reloadData() {
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
        val userId = document.getString("addedBy").parseUserId()
        return AcronymData(
            id = document.id,
            value = document.getString("value") ?: "",
            description = document.getString("description") ?: "",
            addedBy = userId,
        )
    }

    suspend fun addAcronym(acronym: String, description: String, user: String): AcronymData {
        return withContext(Dispatchers.IO) {
            val clearDescription = description.replace("\\", "")
            val values = mapOf(
                "value" to acronym,
                "description" to clearDescription,
                "addedBy" to user,
            )

            firestore.collection(COLLECTION).document(acronym).set(values).get()

            val acronymData = AcronymData(
                acronym,
                acronym,
                clearDescription,
                user.parseUserId()
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

    suspend fun removeAcronym(acronym: AcronymData) {
        lock.lock()
        withContext(Dispatchers.IO) {
            firestore.collection(COLLECTION).document(acronym.value).delete().get()
            cachedData = cachedData?.filter {it.id != acronym.id }
        }
        lock.unlock()
    }

    private fun String?.parseUserId(): Long? {
        if (this == null) {
            return null
        }
        return split(" / ")[1].toLongOrNull()
    }

    companion object {
        const val COLLECTION = "acronyms"
    }
}
