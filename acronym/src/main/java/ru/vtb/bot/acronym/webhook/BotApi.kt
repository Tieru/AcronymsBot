package ru.vtb.bot.acronym.webhook

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.vtb.bot.acronym.app.Bot
import ru.vtb.bot.acronym.app.BotProperties

fun Route.botApi(launchArgs: BotProperties) {
    post("tg/${launchArgs.botToken}") {
        val response = call.receiveText()
        Bot.instance.processUpdate(response)
        call.respond(HttpStatusCode.OK)
    }
}
