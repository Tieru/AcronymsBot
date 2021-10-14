package ru.vtb.bot.acronym.message

import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ParseMode
import ru.vtb.bot.acronym.entity.UserData

fun TextHandlerEnvironment.answer(
    text: String,
    parseMode: ParseMode? = ParseMode.MARKDOWN,
    replyMarkup: InlineKeyboardMarkup? = null
) {
    bot.sendMessage(
        chatId = ChatId.fromId(message.chat.id),
        text = text,
        parseMode = parseMode,
        disableWebPagePreview = true,
        replyMarkup = replyMarkup,
    )
}

fun TextHandlerEnvironment.reply(
    text: String,
    parseMode: ParseMode? = ParseMode.MARKDOWN,
    replyMarkup: InlineKeyboardMarkup? = null
) {
    bot.sendMessage(
        chatId = ChatId.fromId(message.chat.id),
        text = text,
        replyToMessageId = message.messageId,
        parseMode = parseMode,
        disableWebPagePreview = true,
        replyMarkup = replyMarkup,
    )
}

fun TextHandlerEnvironment.getUserData(): UserData {
    val from = message.from ?: error("Не могу вас идентифицировать. Ваш ID скрыт?")
    return UserData(
        id = from.id,
        username = from.username,
        firstName = from.firstName,
        lastName = from.lastName,
    )
}
