package ru.vtb.bot.acronym.injection

import org.koin.dsl.module
import ru.vtb.bot.acronym.app.BotProperties
import ru.vtb.bot.acronym.repository.*

val dataModule = module {
    val firestoreInitializer by lazy { FirestoreInitializer() }
    single { firestoreInitializer }
    factory { firestoreInitializer.getDb() }
    single<AcronymRepository> {
        FirebaseAcronymRepository(
            firestore = get(),
            dataExport = get()
        )
    }

    factory {
        AcronymDataExporter(
            csvWriter = CsvWriter(),
            storePath = get<BotProperties>().exportDataPath,
        )
    }
}
