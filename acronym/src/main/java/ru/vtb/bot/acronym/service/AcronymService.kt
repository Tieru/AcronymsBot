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
        val newAcronymValue = acronym.toLowerCase()
        val existingAcronym = acronymRepository.getAcronyms().find { it.value.toLowerCase() == newAcronymValue }
        if (existingAcronym != null && !user.canWriteAcronym(existingAcronym)) {
            return AddAcronymResult.NotAuthorized
        }

        val acronymData = acronymRepository.addAcronym(
            acronym = acronym,
            description = description,
            user = user.concatUsernameAndId(),
        )
        return if (existingAcronym == null) {
            AddAcronymResult.AcronymCreated(acronymData)
        } else {
            AddAcronymResult.AcronymUpdated(acronymData)
        }
    }

    fun onGetAcronym(acronym: String): GetAcronymResult {
        val requested = acronym.toLowerCase()
        val acronyms = acronymRepository.getAcronyms()
        val found = acronyms.find { it.value.toLowerCase() == requested }
        if (found != null) {
            return GetAcronymResult.Found(found)
        }

        val similar = acronyms.filter { it.value.toLowerCase().contains(requested) }.map { it.value }
        return if (similar.isEmpty()) {
            GetAcronymResult.NotFound
        } else {
            GetAcronymResult.FoundSimilar(similar)
        }
    }

    fun onFindAcronyms(acronym: String): List<AcronymData> {
        val requested = acronym.toLowerCase()
        val acronyms = acronymRepository.getAcronyms()
        return acronyms.filter { it.value.toLowerCase().contains(requested) }
    }

    suspend fun onReloadData() {
        acronymRepository.reloadData()
    }

    suspend fun onRemoveAcronym(user: UserData, acronym: String): DeleteAcronymResult {
        val toBeDeleted = acronym.toLowerCase()
        val foundAcronym = acronymRepository.getAcronyms().find { it.value.toLowerCase() == toBeDeleted }
            ?: return DeleteAcronymResult.NotFound

        if (!user.canWriteAcronym(foundAcronym)) {
            return DeleteAcronymResult.NotAuthorized
        }

        acronymRepository.removeAcronym(foundAcronym)
        return DeleteAcronymResult.Success
    }

    private fun UserData.canWriteAcronym(acronym: AcronymData): Boolean {
        return isAdmin || acronym.addedBy == id
    }
}
