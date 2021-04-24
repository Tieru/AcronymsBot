package ru.vtb.bot.acronym.repository

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient

class FirestoreInitializer {

    fun initialize() {
        val account = javaClass.getResourceAsStream("/acronymsbot-0a901264dd53.json")
        val credentials = GoogleCredentials.fromStream(account)
        val options = FirebaseOptions.builder().setCredentials(credentials).build()
        FirebaseApp.initializeApp(options)
    }

    fun getDb(): Firestore = FirestoreClient.getFirestore()
}
