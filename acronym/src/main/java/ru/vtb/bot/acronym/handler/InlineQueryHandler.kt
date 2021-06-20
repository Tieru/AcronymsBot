package ru.vtb.bot.acronym.handler

import com.github.kotlintelegrambot.dispatcher.handlers.InlineQueryHandlerEnvironment
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.inlinequeryresults.InlineQueryResult
import com.github.kotlintelegrambot.entities.inlinequeryresults.InputMessageContent
import org.slf4j.LoggerFactory
import ru.vtb.bot.acronym.service.AcronymService

class InlineQueryHandler(
    private val service: AcronymService,
) {

    fun onInlineQuery(env: InlineQueryHandlerEnvironment) {
        try {
            onInlineQuerySafe(env)
        } catch (t: Throwable) {
            LOGGER.error("Error handling inline query", t)
        }
    }

    private fun onInlineQuerySafe(env: InlineQueryHandlerEnvironment) {
        val results = service.onFindAcronyms(env.inlineQuery.query)
            .take(10)
            .map {
                InlineQueryResult.Article(
                    id = it.id,
                    inputMessageContent = InputMessageContent.Text(
                        messageText = it.formattedWithMarkdown,
                        parseMode = ParseMode.MARKDOWN,
                    ),
                    title = it.value,
                    description = it.description,
                )

            }
        env.answer(*results.toTypedArray())
    }

    private fun InlineQueryHandlerEnvironment.answer(vararg inlineQueryResults: InlineQueryResult) {
        bot.answerInlineQuery(inlineQuery.id, inlineQueryResults = inlineQueryResults)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(InlineQueryHandler::class.java)
    }
}
