package ru.vtb.bot.acronym.repository

import ru.vtb.bot.acronym.entity.AcronymData
import ru.vtb.bot.acronym.ext.clearMarkdownEscaping

class MockAcronymRepository: AcronymRepository {
    private var cachedData: List<AcronymData> = emptyList()

    override fun getAcronyms(): List<AcronymData> {
        return cachedData
    }

    override suspend fun reloadData() {
    }

    override suspend fun addAcronym(acronym: String, description: String, userId: Long, username: String?): AcronymData {
        val clearDescription = description.clearMarkdownEscaping()
        val acronymData = AcronymData(
            id = acronym,
            value = acronym,
            description = clearDescription,
            addedById = userId,
            addedByUsername = username,
        )

        cachedData = if (cachedData.find { it.value == acronym } == null) {
            cachedData.plus(acronymData)
        } else {
            cachedData.map { if (it.value == acronym) acronymData else it }
        }
        return acronymData
    }

    override suspend fun removeAcronym(acronym: AcronymData) {
        cachedData = cachedData.filter { it.id != acronym.id }
    }

    companion object {
        const val COLLECTION = "acronyms"
    }
}
