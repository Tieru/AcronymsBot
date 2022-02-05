package ru.vtb.bot.acronym.message

import org.junit.Test
import kotlin.test.assertEquals


class CommandParserTest {

    private val commandParser = CommandParser()

    @Test
    fun `тест parseCommand распознает add с одним параметром`() {
        val input = "/add ВТБ"
        val result = commandParser.parseCommand(input, chatId = 0, senderId = 1, replyMessageText = null)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "ВТБ", description = null), result)
    }

    @Test
    fun `тест parseCommand распознает add с двумя параметрами`() {
        val input = "/add ВТБ ВнешТоргБанк"
        val result = commandParser.parseCommand(input, chatId = 0, senderId = 1, replyMessageText = null)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "ВТБ", description = "ВнешТоргБанк"), result)
    }

    @Test
    fun `тест parseCommand распознает report без параметров`() {
        val input = "/report"
        val result = commandParser.parseCommand(input, chatId = 0, senderId = 1, replyMessageText = null)
        assertEquals(UserCommand.ReportCommand(
            replyMessageText = null,
                message = null,
                senderId = 1,
                chatId = 0
        ), result)
    }

    @Test
    fun `тест parseCommand распознает report с параметром`() {
        val input = "/report text"
        val result = commandParser.parseCommand(input, chatId = 0, senderId = 1, replyMessageText = "AAA")
        assertEquals(UserCommand.ReportCommand(
            replyMessageText = "AAA",
            message = "text",
            senderId = 1,
            chatId = 0
        ), result)
    }

    @Test
    fun `тест parseCommand распознает add с acronym в кавычках`() {
        val input = "/add `В Т Б` Внеш Торг Банк"
        val result = commandParser.parseCommand(input, chatId = 0, senderId = 1, replyMessageText = null)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "В Т Б", description = "Внеш Торг Банк"), result)
    }

    @Test
    fun `тест parseCommand распознает add с acronym в кавычках "`() {
        val input = "/add \"В Т Б\" Внеш Торг Банк"
        val result = commandParser.parseCommand(input, chatId = 0, senderId = 1, replyMessageText = null)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "В Т Б", description = "Внеш Торг Банк"), result)
    }

    @Test
    fun `тест parseCommand распознает add с acronym в кавычках «»`() {
        val input = "/add «В Т Б» Внеш Торг Банк"
        val result = commandParser.parseCommand(input, chatId = 0, senderId = 1, replyMessageText = null)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "В Т Б", description = "Внеш Торг Банк"), result)
    }

    @Test
    fun `тест parseCommand распознает add с acronym в кавычках ””`() {
        val input = "/add ”В Т Б” Внеш Торг Банк"
        val result = commandParser.parseCommand(input, chatId = 0, senderId = 1, replyMessageText = null)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "В Т Б", description = "Внеш Торг Банк"), result)
    }
}