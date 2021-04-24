package ru.vtb.bot.acronym.app

import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.vtb.bot.acronym.injection.dataModule
import ru.vtb.bot.acronym.injection.serviceModule

@KoinApiExtension
fun main() {
    val launchOptions = LaunchOptions.initialize()
    startBot(launchOptions)
}

@KoinApiExtension
private fun startBot(launchArgs: LaunchOptions) {
    startKoin {
        printLogger()
        modules(
            module {
                single { launchArgs }
            },
            dataModule,
            serviceModule,
        )
    }

    AppLifecycle.onAppLaunch()
    Bot.instance.startPolling()
}
