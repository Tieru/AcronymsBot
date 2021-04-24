package ru.vtb.bot.acronym.app

data class LaunchOptions(
    val botToken: String,
) {
    companion object {
        fun initialize(): LaunchOptions {
            val botToken = System.getenv("BOT_ACRONYM_TOKEN")
            return LaunchOptions(
                botToken = botToken,
            )
        }
    }
}
