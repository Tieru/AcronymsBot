package ru.vtb.bot.acronym.message

sealed class UserCommand {

    object StartCommand : UserCommand()
    data class AddAcronymCommand(val acronym: String?, val description: String?) : UserCommand()
    data class DeleteAcronymCommand(val acronym: String?) : UserCommand()
    object ReloadCommand : UserCommand()
    object MarkUpHelpCommand : UserCommand()
    object Help : UserCommand()
    data class ReportCommand(val replyMessageText: String?, val message: String?, val senderId: Long?, val chatId: Long) : UserCommand()

    object UnknownCommand : UserCommand()

    companion object {
        const val START_COMMAND = "/start"
        const val ADD_COMMAND = "/add"
        const val DELETE_COMMAND = "/delete"
        const val RELOAD_COMMAND = "/reload"
        const val MARKDOWN_COMMAND = "/markdown"
        const val HELP_COMMAND = "/help"
        const val REPORT_COMMAND = "/report"
    }
}
