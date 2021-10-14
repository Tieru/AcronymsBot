package ru.vtb.bot.acronym.app

data class BotProperties(
    val botToken: String,
    val dashbotToken: String?,
    val webhookUrl: String?,
    val webhookHost: String?,
    val supportChatId: Long?,
) {
    companion object {
        fun initialize(): BotProperties {
            val botToken = System.getenv("BOT_ACRONYM_TOKEN")
            val dashbotToken = System.getenv("DASHBOT_TOKEN")
            val webhookUrl = System.getenv("WEBHOOK_URL")
            val webhookHost = System.getenv("WEBHOOK_HOST")
            val supportChatId = System.getenv("SUPPORT_CHAT_ID").toLongOrNull()
            return BotProperties(
                botToken = botToken,
                dashbotToken = dashbotToken,
                webhookUrl = webhookUrl,
                webhookHost = webhookHost,
                supportChatId = supportChatId,
            )
        }
    }
}
