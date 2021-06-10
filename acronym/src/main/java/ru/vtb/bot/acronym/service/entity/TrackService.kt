package ru.vtb.bot.acronym.service.entity

import io.ktor.client.call.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.vtb.bot.acronym.client.DashbotClient
import ru.vtb.bot.acronym.entity.track.Intent
import ru.vtb.bot.acronym.entity.track.TrackRequest
import javax.annotation.PostConstruct

class TrackService(
    private val enabled: Boolean,
    private val trackClient: DashbotClient
) {

    private val incomingQueue: Channel<TrackRequest> = Channel()
    private val outgoingQueue: Channel<TrackRequest> = Channel()

    fun trackIncoming(chatId: Long, action: String, text: String = "__placeholder__") {
        incomingQueue.offer(TrackRequest(text, chatId.toString(), Intent(action)))
    }

    fun trackOutgoing(chatId: Long, action: String, text: String = "__placeholder__") {
        outgoingQueue.offer(TrackRequest(text, chatId.toString(), Intent(action)))
    }

    fun init()  {
        if (!enabled) return
        GlobalScope.launch {
            while (this.isActive) {
                val request = incomingQueue.receive()
                try {
                    val response = trackClient.incoming(request)
                    logger.info("Status: ${response.status}, response: ${response.receive<String>()}")
                } catch (e: Exception) {
                    logger.error(e.message, e)
                }
            }
        }
        GlobalScope.launch {
            while (this.isActive) {
                val request = outgoingQueue.receive()
                try {
                    val response = trackClient.outgoing(request)
                    logger.info("Status: ${response.status}, response: ${response.receive<String>()}")
                } catch (e: Exception) {
                    logger.error(e.message, e)
                }
            }
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TrackService::class.java)
    }

}