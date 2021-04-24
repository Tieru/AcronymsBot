package ru.vtb.bot.acronym.service.entity

sealed class DeleteAcronymResult {
    object Success : DeleteAcronymResult()
    object NotFound : DeleteAcronymResult()
    object NotAuthorized : DeleteAcronymResult()
}
