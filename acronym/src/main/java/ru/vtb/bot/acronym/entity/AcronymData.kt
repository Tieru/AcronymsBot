package ru.vtb.bot.acronym.entity

data class AcronymData(
    val id: String,
    val value: String,
    val description: String,
    val addedBy: Long?,
) {
    fun formatWithMarkdown(): String {
        return "*$value*\n${description.replace("\\", "")}"
    }
}
