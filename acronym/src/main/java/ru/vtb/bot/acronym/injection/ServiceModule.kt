package ru.vtb.bot.acronym.injection

import org.koin.dsl.module
import ru.vtb.bot.acronym.handler.InlineQueryHandler
import ru.vtb.bot.acronym.message.CommandParser
import ru.vtb.bot.acronym.message.GeneralMessageHandler
import ru.vtb.bot.acronym.service.AcronymService
import ru.vtb.bot.acronym.service.entity.TrackService

val serviceModule = module {
    single { CommandParser() }
    single { AcronymService(get()) }
    single { GeneralMessageHandler(get(), get(), get()) }
    single { InlineQueryHandler(get()) }
}
