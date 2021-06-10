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
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.4")
    implementation("org.koin:koin-core:2.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("com.google.cloud:google-cloud-firestore:2.2.5")
    implementation("com.google.firebase:firebase-admin:7.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.3")
    implementation("io.ktor:ktor-client-jackson:1.5.4")
    implementation("io.ktor:ktor-client-core:1.5.4")
    implementation("io.ktor:ktor-client-cio:1.5.4")


    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-core:1.2.3")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.codehaus.janino:janino:3.1.3")

    testImplementation(kotlin("test-junit"))
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