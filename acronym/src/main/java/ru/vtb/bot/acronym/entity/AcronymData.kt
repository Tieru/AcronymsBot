package ru.vtb.bot.acronym.entity

import ru.vtb.bot.acronym.ext.clearMarkdownEscaping

data class AcronymData(
    val id: String,
    val value: String,
    val description: String,
    val addedById: Long?,
    val addedByUsername: String?,
) {
    val formattedWithMarkdown by lazy {
        val formattedDescription = description.clearMarkdownEscaping().replace("\\n", "\n")
        "*$value*\n$formattedDescription"
    }
}
