package ru.vtb.bot.acronym.message

import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals


class CommandParserTest {

    private val commandParser = CommandParser()

    @Test
    fun `тест parseCommand распознает add с одним параметром`() {
        val input = "/add ВТБ"
        val result = commandParser.parseCommand(input)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "ВТБ", description = null), result)
    }

    @Test
    fun `тест parseCommand распознает add с двумя параметрами`() {
        val input = "/add ВТБ ВнешТоргБанк"
        val result = commandParser.parseCommand(input)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "ВТБ", description = "ВнешТоргБанк"), result)
    }

    @Ignore
    @Test
    fun `тест parseCommand распознает add с acronym в кавычках`() {
        val input = "/add `В Т Б` Внеш Торг Банк"
        val result = commandParser.parseCommand(input)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "В Т Б", description = "Внеш Торг Банк"), result)
    }

    @Ignore
    @Test
    fun `тест parseCommand распознает add с acronym в кавычках "`() {
        val input = "/add \"В Т Б\" Внеш Торг Банк"
        val result = commandParser.parseCommand(input)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "В Т Б", description = "Внеш Торг Банк"), result)
    }

    @Ignore
    @Test
    fun `тест parseCommand распознает add с acronym в кавычках «»`() {
        val input = "/add «В Т Б» Внеш Торг Банк"
        val result = commandParser.parseCommand(input)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "В Т Б", description = "Внеш Торг Банк"), result)
    }

    @Ignore
    @Test
    fun `тест parseCommand распознает add с acronym в кавычках ””`() {
        val input = "/add ”В Т Б” Внеш Торг Банк"
        val result = commandParser.parseCommand(input)
        assertEquals(UserCommand.AddAcronymCommand(acronym = "В Т Б", description = "Внеш Торг Банк"), result)
    }
}