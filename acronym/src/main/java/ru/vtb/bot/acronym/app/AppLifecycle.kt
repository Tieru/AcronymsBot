package ru.vtb.bot.acronym.app

import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.vtb.bot.acronym.repository.AcronymRepository
import ru.vtb.bot.acronym.repository.FirestoreInitializer
import ru.vtb.bot.acronym.service.entity.TrackService

object AppLifecycle : KoinComponent {

    fun onAppLaunch() {
        val firestoreInitializer by inject<FirestoreInitializer>()
        firestoreInitializer.initialize()

        val trackService by inject<TrackService>()
        trackService.init()

        val acronymRepository by inject<AcronymRepository>()
        runBlocking {
            acronymRepository.reloadData()
        }
    }
}