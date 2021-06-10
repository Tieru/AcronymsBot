package ru.vtb.bot.acronym.app

data class BotProperties(
    val botToken: String,
    val dashbotToken: String?
) {
    companion object {
        fun initialize(): BotProperties {
            val botToken = System.getenv("BOT_ACRONYM_TOKEN")
            val dashbotToken = System.getenv("DASHBOT_TOKEN")
            return BotProperties(
                botToken = botToken,
                dashbotToken = dashbotToken
            )
        }
    }
}
