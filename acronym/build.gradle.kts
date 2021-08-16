import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

version = "1.0"

application {
    mainClass.set("ru.vtb.bot.acronym.app.AppKt")
    mainClassName = mainClass.get()
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

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {

    archiveBaseName.set(project.name)
    archiveClassifier.set("")

    mergeServiceFiles()
    exclude { file ->
        file.path.endsWith(".pom") ||
                file.path.endsWith(".dll") ||
                file.path.endsWith("_module") ||
                file.path.contains("LICENSE")
    }

    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClass,
                "Multi-Release" to true,
                "Title" to project.name,
                "Version" to project.version
            )
        )
    }
}