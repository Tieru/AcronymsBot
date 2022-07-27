import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
}

version = "1.0"
group = "ru.vtb.bot.acronym"

application {
    mainClass.set("ru.vtb.bot.acronym.app.AppKt")
}

dependencies {
    implementation(kotlin(Kotlin.stdLib))
    implementation(Dependencies.telegramBot)

    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesJdk)
    implementation(Dependencies.firestore)
    implementation(Dependencies.firebaseAdmin)
    implementation(Dependencies.koin)
    implementation(Dependencies.ktorJackson)
    implementation(Dependencies.ktorClientCore)
    implementation(Dependencies.ktorCio)
    implementation(Dependencies.ktorServerCore)
    implementation(Dependencies.ktorNetty)
    implementation(Dependencies.logbackCore)
    implementation(Dependencies.logbackClassic)
    implementation(Dependencies.logFileJanino)
    implementation(Dependencies.slf4j)

    testImplementation(kotlin(Kotlin.unitTests))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
