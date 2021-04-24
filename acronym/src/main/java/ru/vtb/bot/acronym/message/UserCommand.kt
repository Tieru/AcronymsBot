package ru.vtb.bot.acronym.message

sealed class UserCommand {

    object StartCommand : UserCommand()
    data class AddAcronymCommand(val acronym: String?, val description: String?) : UserCommand()
    data class RemoveAcronymCommand(val acronym: String?) : UserCommand()
    object ReloadCommand : UserCommand()
    object MarkUpHelpCommand : UserCommand()
    object Help : UserCommand()

    object UnknownCommand : UserCommand()
}
