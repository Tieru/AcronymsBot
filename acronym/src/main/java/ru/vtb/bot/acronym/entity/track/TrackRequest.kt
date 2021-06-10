package ru.vtb.bot.acronym.entity.track

data class TrackRequest(
    val text: String,
    val userId: String,
    val intent: Intent? = null,
    val platformJson: Map<String, Any>? = null,
    val platformUserJson: Map<String, Any>? = null,
    val sessionId: String? = null
)