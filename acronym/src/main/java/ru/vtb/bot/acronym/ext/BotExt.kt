package ru.vtb.bot.acronym.ext

import com.github.kotlintelegrambot.entities.User

val User?.usernameOrName: String
    get() {
        if (this == null) {
            return "UnknownUser"
        }

        val title = if (username.isNullOrBlank()) {
            "$firstName ${lastName ?: ""}".trim()
        } else {
            "@$username"
        }

        return "$title / $id"
    }
