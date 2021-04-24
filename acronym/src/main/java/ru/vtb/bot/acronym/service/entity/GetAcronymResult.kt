package ru.vtb.bot.acronym.service.entity

import ru.vtb.bot.acronym.entity.AcronymData

sealed class GetAcronymResult {
    data class Found(val acronym: AcronymData) : GetAcronymResult()
    object NotFound : GetAcronymResult()
    data class FoundSimilar(val similar: List<String>) : GetAcronymResult()
}
