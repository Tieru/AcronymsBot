package ru.vtb.bot.acronym.message

class CommandParser {

    fun parseCommand(text: String): UserCommand {
        assert(text.startsWith("/"))

        return when (text) {
            "/start" -> UserCommand.StartCommand
            "/markdown" -> UserCommand.MarkUpHelpCommand
            "/help" -> UserCommand.Help
            "/add" -> UserCommand.AddAcronymCommand(acronym = null, description = null)
            "/reload" -> UserCommand.ReloadCommand
            "/delete" -> UserCommand.RemoveAcronymCommand(acronym = null)
            else -> tryParseComplicatedCommand(text)
        }
    }

    private fun tryParseComplicatedCommand(text: String): UserCommand {
        if (text.startsWith("/add")) {
            val args = text.substring("/add ".length).split(" ")
            val acronym = args.firstOrNull()
            val description = args.drop(1).joinToString(separator = " ").trim().takeIf { it.isNotBlank() }
            return UserCommand.AddAcronymCommand(acronym, description)
        }

        if (text.startsWith("/delete")) {
            val args = text.substring("/delete ".length).split(" ")
            val acronym = args.firstOrNull()
            return UserCommand.RemoveAcronymCommand(acronym)
        }

        return UserCommand.UnknownCommand
    }
}
