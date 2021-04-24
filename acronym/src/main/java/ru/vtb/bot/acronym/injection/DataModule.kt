package ru.vtb.bot.acronym.injection

import org.koin.dsl.module
import ru.vtb.bot.acronym.repository.AcronymRepository
import ru.vtb.bot.acronym.repository.FirestoreInitializer

val dataModule = module {
    val firestoreInitializer by lazy { FirestoreInitializer() }
    single { firestoreInitializer }
    factory { firestoreInitializer.getDb() }
    single { AcronymRepository(get()) }
}
