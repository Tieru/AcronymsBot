package ru.vtb.bot.acronym.app

data class BotProperties(
    val botToken: String,
    val dashbotToken: String?,
    val webhookUrl: String?,
    val webhookHost: String?,
    val supportChatId: Long?,
    val exportDataPath: String?,
) {
    companion object {
        fun initialize(): BotProperties {
            val botToken = System.getenv("BOT_ACRONYM_TOKEN")
            val dashbotToken = optEnv("DASHBOT_TOKEN")
            val webhookUrl = optEnv("WEBHOOK_URL")
            val webhookHost = optEnv("WEBHOOK_HOST")
            val supportChatId = optEnv("SUPPORT_CHAT_ID")?.toLongOrNull()
            val exportDataPath = optEnv("EXPORT_DATA_PATH")
            return BotProperties(
                botToken = botToken,
                dashbotToken = dashbotToken,
                webhookUrl = webhookUrl,
                webhookHost = webhookHost,
                supportChatId = supportChatId,
                exportDataPath = exportDataPath,
            )
        }

        private fun optEnv(name: String): String? {
            return System.getenv()[name]
        }
    }
}
