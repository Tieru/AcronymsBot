package ru.vtb.bot.acronym.service.entity

sealed class ReportResult {
    class SendSupportMessage(val supportChatId: Long, val msg: String) : ReportResult()
    object Error : ReportResult()
    object ShowHelp : ReportResult()
}
