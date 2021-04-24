package ru.vtb.bot.acronym.service.entity

import ru.vtb.bot.acronym.entity.AcronymData

sealed class AddAcronymResult {
    data class AcronymCreated(val acronym: AcronymData) : AddAcronymResult()
    data class AcronymUpdated(val acronym: AcronymData) : AddAcronymResult()
    object NotAuthorized : AddAcronymResult()
}
