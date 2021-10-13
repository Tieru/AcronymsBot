package ru.vtb.bot.acronym.repository

import ru.vtb.bot.acronym.entity.AcronymData

interface AcronymRepository {
    fun getAcronyms(): List<AcronymData>

    suspend fun reloadData()

    suspend fun addAcronym(acronym: String, description: String, user: String): AcronymData

    suspend fun removeAcronym(acronym: AcronymData)
}
