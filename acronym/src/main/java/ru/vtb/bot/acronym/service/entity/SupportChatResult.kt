package ru.vtb.bot.acronym.service.entity

sealed class SupportChatResult {
    object None : SupportChatResult()
    class SendMessage(val userId: Long, val text: String) : SupportChatResult()
    class Error(val text: String) : SupportChatResult()
}
