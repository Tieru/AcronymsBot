object Versions {
    const val coroutines = "1.5.1"
    const val firebaseAdmin = "7.1.1"
    const val firestore = "2.2.5"
    const val kotlin = "1.5.20"
    const val ktor = "1.6.1"
    const val koin = "3.1.2"
    const val logback = "1.2.3"
    const val logFileJanino = "3.1.3"
    const val slf4j = "1.7.30"
    const val telegramBot = "6.0.6"
}

object Kotlin {
    const val stdLib = "stdlib-jdk8"
    const val unitTests = "test-junit"
}

object Dependencies {
    // Telegram
    const val telegramBot = "io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:${Versions.telegramBot}"

    // DI
    const val koin = "io.insert-koin:koin-core:${Versions.koin}"

    // Coroutines
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesJdk = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${Versions.coroutines}"

    // Firebase
    const val firestore = "com.google.cloud:google-cloud-firestore:${Versions.firestore}"
    const val firebaseAdmin = "com.google.firebase:firebase-admin:${Versions.firebaseAdmin}"

    // Ktor
    const val ktorCio = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val ktorClientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val ktorJackson = "io.ktor:ktor-client-jackson:${Versions.ktor}"
    const val ktorNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
    const val ktorServerCore = "io.ktor:ktor-server-core:${Versions.ktor}"

    // Logging
    const val slf4j = "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val logbackCore = "ch.qos.logback:logback-core:${Versions.logback}"
    const val logbackClassic = "ch.qos.logback:logback-classic:${Versions.logback}"
    const val logFileJanino = "org.codehaus.janino:janino:${Versions.logFileJanino}"
}
