package ru.vtb.bot.acronym.injection

import org.koin.dsl.module
import ru.vtb.bot.acronym.app.BotProperties
import ru.vtb.bot.acronym.handler.InlineQueryHandler
import ru.vtb.bot.acronym.message.CommandParser
import ru.vtb.bot.acronym.message.GeneralMessageHandler
import ru.vtb.bot.acronym.service.AcronymService
import ru.vtb.bot.acronym.service.ReportService

val serviceModule = module {
    single { CommandParser() }
    single { AcronymService(get()) }
    single { ReportService(supportChatId = get<BotProperties>().supportChatId) }
    single {
        GeneralMessageHandler(
            commandParser = get(),
            acronymService = get(),
            reportService = get(),
            launchProperties = get(),
            trackService = get(),
        )
    }
    single { InlineQueryHandler(get()) }
}
