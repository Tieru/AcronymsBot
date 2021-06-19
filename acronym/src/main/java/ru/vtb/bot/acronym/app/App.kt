package ru.vtb.bot.acronym.app

import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.vtb.bot.acronym.injection.dataModule
import ru.vtb.bot.acronym.injection.serviceModule
import ru.vtb.bot.acronym.injection.trackModule

@KoinApiExtension
fun main() {
    val launchOptions = BotProperties.initialize()
    startBot(launchOptions)
}

@KoinApiExtension
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
    Bot.instance.startPolling()
}
