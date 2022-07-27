package ru.vtb.bot.acronym.service

import ru.vtb.bot.acronym.entity.AcronymData
import ru.vtb.bot.acronym.entity.UserData
import ru.vtb.bot.acronym.repository.AcronymRepository
import ru.vtb.bot.acronym.service.entity.AddAcronymResult
import ru.vtb.bot.acronym.service.entity.DeleteAcronymResult
import ru.vtb.bot.acronym.service.entity.GetAcronymResult

class AcronymService(
    private val acronymRepository: AcronymRepository,
) {

    suspend fun onAddAcronym(user: UserData, acronym: String, description: String): AddAcronymResult {
        val newAcronymValue = acronym.lowercase()
        val existingAcronym = acronymRepository.getAcronyms().find { it.value.lowercase() == newAcronymValue }
        if (existingAcronym != null && !user.canWriteAcronym(existingAcronym)) {
            return AddAcronymResult.NotAuthorized
        }

        val (userId, username) = existingAcronym?.let {
            (it.addedById ?: 0) to it.addedByUsername.orEmpty()
        } ?: (user.id to user.username)

        val acronymData = acronymRepository.addAcronym(
            acronym = acronym,
            description = description,
            userId = userId,
            username = username,
        )
        return if (existingAcronym == null) {
            AddAcronymResult.AcronymCreated(acronymData)
        } else {
            AddAcronymResult.AcronymUpdated(acronymData)
        }
    }

    fun onGetAcronym(acronym: String): GetAcronymResult {
        val requested = acronym.lowercase()
        val acronyms = acronymRepository.getAcronyms()
        val found = acronyms.find { it.value.lowercase() == requested }
        if (found != null) {
            return GetAcronymResult.Found(found)
        }

        val similar = acronyms.filter { it.value.lowercase().contains(requested) }.map { it.value }
        return if (similar.isEmpty()) {
            GetAcronymResult.NotFound
        } else {
            GetAcronymResult.FoundSimilar(similar)
        }
    }

    fun onFindAcronyms(acronym: String): List<AcronymData> {
        val requested = acronym.lowercase()
        val acronyms = acronymRepository.getAcronyms()
        return acronyms.filter { it.value.lowercase().contains(requested) }
    }

    suspend fun onReloadData() {
        acronymRepository.reloadData()
    }

    suspend fun onRemoveAcronym(user: UserData, acronym: String): DeleteAcronymResult {
        val toBeDeleted = acronym.lowercase()
        val foundAcronym = acronymRepository.getAcronyms().find { it.value.lowercase() == toBeDeleted }
            ?: return DeleteAcronymResult.NotFound

        if (!user.canWriteAcronym(foundAcronym)) {
            return DeleteAcronymResult.NotAuthorized
        }

        acronymRepository.removeAcronym(foundAcronym)
        return DeleteAcronymResult.Success
    }

    private fun UserData.canWriteAcronym(acronym: AcronymData): Boolean {
        return isAdmin || acronym.addedById == id
    }
}
