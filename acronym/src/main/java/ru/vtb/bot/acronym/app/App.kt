package ru.vtb.bot.acronym.app

import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.slf4j.LoggerFactory
import ru.vtb.bot.acronym.injection.dataModule
import ru.vtb.bot.acronym.injection.serviceModule
import ru.vtb.bot.acronym.injection.trackModule
import ru.vtb.bot.acronym.webhook.botApi

fun main() {
    val launchOptions = BotProperties.initialize()
    startBot(launchOptions)
}

private fun startBot(launchArgs: BotProperties) {
    startKoin {
        printLogger()
        modules(
            module {
                single { launchArgs }
            },
            dataModule,
            trackModule,
            serviceModule,
        )
    }

    AppLifecycle.onAppLaunch()

    val logger = LoggerFactory.getLogger("MAIN")

    // Polling
    /*try {
        Bot.instance.startPolling()
    } catch (e: Throwable) {
        logger.error("On top lvl exception", e)
    }*/

    // Webhook
    if (!Bot.instance.startWebhook()) {
        logger.error("Unable to start webhook. Stopping")
        return
    }

    logger.info("Webhook started successfully")

    val webhookHost = launchArgs.webhookHost ?: error("WEBHOOK_HOST env was not set")
    embeddedServer(Netty, port = 443, host = webhookHost) {
        routing {
            botApi(launchArgs)
        }
    }.start(wait = true)
}
