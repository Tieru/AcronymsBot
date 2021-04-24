package ru.vtb.bot.acronym.app

import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.vtb.bot.acronym.repository.AcronymRepository
import ru.vtb.bot.acronym.repository.FirestoreInitializer

@KoinApiExtension
object AppLifecycle : KoinComponent {

    fun onAppLaunch() {
        val firestoreInitializer by inject<FirestoreInitializer>()
        firestoreInitializer.initialize()

        val acronymRepository by inject<AcronymRepository>()
        runBlocking {
            acronymRepository.reloadData()
        }
    }
}