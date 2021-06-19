package ru.vtb.bot.acronym.injection

import org.koin.dsl.module
import ru.vtb.bot.acronym.client.DashbotClient
import ru.vtb.bot.acronym.service.entity.TrackService

val trackModule = module {
    single { DashbotClient(get()) }
    single { TrackService(true, get()) }
}