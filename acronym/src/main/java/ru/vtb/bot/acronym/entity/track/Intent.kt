
package ru.vtb.bot.acronym.entity.track

data class Intent(
    val name: String,
    val inputs: List<Input>? = null,
    val confidence: Float? = null
)