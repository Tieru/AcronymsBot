package ru.vtb.bot.acronym.message

class CommandParser {

    fun parseCommand(text: String, chatId: Long, senderId: Long?, replyMessageText: String?): UserCommand {
        assert(text.startsWith("/"))

        return when (text) {
            UserCommand.START_COMMAND -> UserCommand.StartCommand
            UserCommand.ADD_COMMAND -> UserCommand.AddAcronymCommand(acronym = null, description = null)
            UserCommand.REPORT_COMMAND ->
                UserCommand.ReportCommand(
                    replyMessageText = replyMessageText,
                    message = null,
                    chatId = chatId,
                    senderId = senderId,
                )
            UserCommand.HELP_COMMAND -> UserCommand.Help
            UserCommand.MARKDOWN_COMMAND -> UserCommand.MarkUpHelpCommand
            UserCommand.DELETE_COMMAND -> UserCommand.DeleteAcronymCommand(acronym = null)
            UserCommand.RELOAD_COMMAND -> UserCommand.ReloadCommand
            else -> tryParseComplicatedCommand(text, chatId = chatId, senderId = senderId, replyMessageText = replyMessageText)
        }
    }

    private fun tryParseComplicatedCommand(text: String, chatId: Long, senderId: Long?, replyMessageText: String?): UserCommand {
        return when {
            text.startsWith(UserCommand.REPORT_COMMAND) -> {
                val args = text.fetchMessageArgs(UserCommand.REPORT_COMMAND)
                return UserCommand.ReportCommand(
                    replyMessageText = replyMessageText,
                    message = args.joinToString(" "),
                    chatId = chatId,
                    senderId = senderId,
                )
            }

            text.startsWith(UserCommand.ADD_COMMAND) -> {
                val args = text.substring("${UserCommand.ADD_COMMAND} ".length)
                val quoted = ADD_CMD_QUOTES_REGEX.find(args)
                val (acronym: String?, description) = if (quoted != null && quoted.groupValues.size == 3) {
                    quoted.groupValues[1].trim() to quoted.groupValues[2].trim()
                } else {
                    val parts = args.split(" ")
                    parts.firstOrNull() to parts.drop(1).joinToString(separator = " ").trim().takeIf { it.isNotBlank() }
                }
                return UserCommand.AddAcronymCommand(acronym, description)
            }

            text.startsWith(UserCommand.DELETE_COMMAND) -> {
                val args = text.fetchMessageArgs(UserCommand.DELETE_COMMAND)
                val acronym = args.firstOrNull()
                return UserCommand.DeleteAcronymCommand(acronym)
            }

            else -> UserCommand.UnknownCommand
        }
    }

    private fun String.fetchMessageArgs(command: String): List<String> = substring("$command ".length).split(" ")

    companion object {
        private val ADD_CMD_QUOTES_REGEX = Regex("[\"”'`«](.*)[\"”'`»] (.+)")
    }
}
