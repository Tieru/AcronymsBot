package ru.vtb.bot.acronym.service

import com.github.kotlintelegrambot.entities.Message
import ru.vtb.bot.acronym.entity.UserData
import ru.vtb.bot.acronym.message.UserCommand
import ru.vtb.bot.acronym.service.entity.ReportResult
import ru.vtb.bot.acronym.service.entity.SupportChatResult

class ReportService(
    private val supportChatId: Long?,
) {

    fun onReport(user: UserData, command: UserCommand.ReportCommand): ReportResult {
        if (command.message.isNullOrEmpty() && command.replyMessageText.isNullOrEmpty()) {
            return ReportResult.ShowHelp
        }

        val supportChatId = supportChatId ?: return ReportResult.Error

        val userInfoPart = user.concatUsernameAndId() + "\n"
        val msgPart = command.message?.let { "Msg: $it\n" }
        val replyMsgPart = command.replyMessageText?.let { "Reply: ${it.take(40)}" }
        val fullMessage = userInfoPart + msgPart.orEmpty() + replyMsgPart.orEmpty()
        return ReportResult.SendSupportMessage(supportChatId, fullMessage)
    }

    fun onSupportChatResponse(user: UserData, replyMessage: Message?, text: String): SupportChatResult {
        val replyText = replyMessage?.text
        if (replyText == null || !user.isAdmin) {
            return SupportChatResult.None
        }

        val userId = parseUserIdFromSupportChatReply(replyText) ?: return SupportChatResult.Error("Не удалось получить userId")
        return SupportChatResult.SendMessage(userId, text)
    }

    private fun parseUserIdFromSupportChatReply(replyText: String): Long? {
        return replyText.split("\n").take(1)
            .firstOrNull()
            ?.split(" / ")
            ?.getOrNull(1)
            ?.toLongOrNull()
    }
}
