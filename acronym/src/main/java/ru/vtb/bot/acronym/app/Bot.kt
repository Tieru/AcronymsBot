package ru.vtb.bot.acronym.app

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.inlineQuery
import com.github.kotlintelegrambot.dispatcher.telegramError
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.logging.LogLevel
import com.github.kotlintelegrambot.webhook
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import ru.vtb.bot.acronym.handler.InlineQueryHandler
import ru.vtb.bot.acronym.message.GeneralMessageHandler

object Bot : KoinComponent {

    private val LOGGER = LoggerFactory.getLogger(Bot::class.java)
    private val messageHandler by inject<GeneralMessageHandler>()
    private val inlineQueryHandler by inject<InlineQueryHandler>()
    private val properties by inject<BotProperties>()

    val instance by lazy {
        bot {
            token = properties.botToken
            logLevel = LogLevel.Error

            properties.webhookUrl?.let { webhookUrl ->
                webhook {
                    url = webhookUrl
                    maxConnections = 50
                    allowedUpdates = listOf("message", "inline_query")
                }
            }

            dispatch {
                inlineQuery {
                    inlineQueryHandler.onInlineQuery(this)
                    update.consume()
                }

                text {
                    messageHandler.onMessage(this@text)
                    update.consume()
                }

                telegramError {
                    LOGGER.error(error.getErrorMessage())
                }
            }
        }
    }
}
