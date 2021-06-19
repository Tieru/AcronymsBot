package ru.vtb.bot.acronym.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ru.vtb.bot.acronym.app.BotProperties
import ru.vtb.bot.acronym.entity.track.TrackRequest

class DashbotClient(
    private val props: BotProperties
) {

    private val httpClient: HttpClient = HttpClient(CIO) {
        defaultRequest {
            url { protocol = URLProtocol("https", 443); host = "tracker.dashbot.io" }
            contentType(ContentType.Application.Json)
        }
        install(JsonFeature)
    }

    private val REST_VERSION = "10.1.1-rest"
    private val PLATFORM = "universal"

    suspend fun incoming(trackRequest: TrackRequest): HttpResponse {
        return track(trackRequest, Direction.INCOMING)
    }

    suspend fun outgoing(trackRequest: TrackRequest): HttpResponse {
        return track(trackRequest, Direction.OUTGOING)
    }

    private suspend fun track(trackRequest: TrackRequest, direction: Direction): HttpResponse {
        return httpClient
            .post() {
                url { encodedPath = "/track" }
                parameter("platform", PLATFORM)
                parameter("v", REST_VERSION)
                parameter("type", direction.type)
                parameter("apiKey", props.dashbotToken)
                body = trackRequest
            }
    }

    private enum class Direction(val type: String) {
        INCOMING("incoming"),
        OUTGOING("outgoing"),
    }

}