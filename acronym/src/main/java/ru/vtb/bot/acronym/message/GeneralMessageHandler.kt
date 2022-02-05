package ru.vtb.bot.acronym.message

import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.vtb.bot.acronym.app.BotProperties
import ru.vtb.bot.acronym.ext.usernameOrName
import ru.vtb.bot.acronym.service.AcronymService
import ru.vtb.bot.acronym.service.ReportService
import ru.vtb.bot.acronym.service.entity.*

class GeneralMessageHandler(
    private val commandParser: CommandParser,
    private val acronymService: AcronymService,
    private val reportService: ReportService,
    private val trackService: TrackService,
    private val launchProperties: BotProperties,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    fun onMessage(environment: TextHandlerEnvironment) {
        if (environment.shouldBeIgnored()) {
            return
        }

        val handler = CoroutineExceptionHandler { _, exception ->
            LOGGER.error("On msg handle error ${environment.message.messageId}", exception)
            environment.answer("Произошла ошибка")
        }

        val loggedText = environment.text.take(10)
        LOGGER.debug("On msg ${environment.message.messageId} '$loggedText'")

        GlobalScope.launch(handler) {
            handleMessage(environment)

            LOGGER.debug("On msg handled successfully ${environment.message.messageId}")
        }
    }

    private suspend fun handleMessage(environment: TextHandlerEnvironment) = withContext(defaultDispatcher) {
        if (environment.isSupportChatMessage()) {
            onSupportChatMessage(environment)
            return@withContext
        }

        val isCommand = environment.text.startsWith("/")
        if (isCommand) {
            handleCommand(environment)
        } else {
            onGetAcronym(environment)
        }
    }

    private suspend fun handleCommand(environment: TextHandlerEnvironment) {
        val command = commandParser.parseCommand(
            environment.text,
            chatId = environment.message.chat.id,
            senderId = environment.message.from?.id,
            replyMessageText = environment.message.replyToMessage?.text,
        )
        when (command) {
            is UserCommand.StartCommand -> onStartCommand(environment)
            is UserCommand.ReportCommand -> onReport(environment, command)
            UserCommand.Help -> onHelpCommand(environment)
            is UserCommand.AddAcronymCommand -> onAddAcronymCommand(environment, command.acronym, command.description)
            UserCommand.MarkUpHelpCommand -> onMarkUpHelpCommand(environment)
            UserCommand.ReloadCommand -> onReload(environment)
            is UserCommand.DeleteAcronymCommand -> onRemoveAcronymCommand(environment, command.acronym)
            UserCommand.UnknownCommand -> onUnknownCommand(environment)
        }
    }

    private fun onGetAcronym(environment: TextHandlerEnvironment) {
        trackService.trackIncoming(environment.message.chat.id, "Get acronym", environment.text.take(15))
        val result = acronymService.onGetAcronym(environment.text)
        when (result) {
            GetAcronymResult.NotFound -> environment.answer("Аббревиатура не найдена")
            is GetAcronymResult.Found -> environment.answer(result.acronym.formattedWithMarkdown)
            is GetAcronymResult.FoundSimilar -> {
                val similar = result.similar.joinToString(separator = "*, *")
                environment.answer("Точных совпадений не найдено.\nПохожие варианты: *${similar}*")
            }
        }
    }

    private fun onStartCommand(environment: TextHandlerEnvironment) {
        LOGGER.info("User ${environment.message.from?.usernameOrName} joined bot")
        trackService.trackIncoming(environment.message.chat.id, "Start", environment.text)
        environment.answer("Здравствуйте.\nОтправляйте мне аббревиатуры, и я постараюсь найти, что они значат")
    }

    private fun onHelpCommand(environment: TextHandlerEnvironment) {
        trackService.trackIncoming(environment.message.chat.id, "Help", environment.text)
        val keyboard = InlineKeyboardMarkup.createSingleButton(
            InlineKeyboardButton.SwitchInlineQueryCurrentChat(
                text = "Перейти в inline режим",
                switchInlineQueryCurrentChat = "ВТБ"
            )
        )
        environment.answer(
            text = "Для добавления аббревиатуры используйте /add\nДля ее удаления /delete\n\nОтправляйте название аббревиатуры в личном чате со мной, чтобы я попытался найти ее значение.\nПомимо этого вы можете использовать inline запросы для более удобного поиска и шаринга аббревиатур (будет работать в любых чатах)",
            replyMarkup = keyboard,
        )
    }

    private fun onMarkUpHelpCommand(environment: TextHandlerEnvironment) {
        trackService.trackIncoming(environment.message.chat.id, "Markdown help", environment.text)
        environment.answer("Примеры форматирования. Обратите внимание, что спец символы нужно 'ескейпать' обратными слэшами\n\n*Жирный текст* - \\\\*жирный текст\\\\*\n_Курсив_ - \\\\_Курсив\\\\_\n[Ссылка](http://example.com) - \\\\[Ссылка](http://example.com)")
    }

    private suspend fun onReload(environment: TextHandlerEnvironment) {
        if (environment.getUserData().isAdmin) {
            acronymService.onReloadData()
            environment.answer("Данные обновлены")
        } else {
            environment.answer("Отсутствуют права на выполнение этого действия")
        }
    }

    private suspend fun onAddAcronymCommand(environment: TextHandlerEnvironment, acronym: String?, description: String?) {
        trackService.trackIncoming(environment.message.chat.id, "Add", environment.text)
        if (acronym.isNullOrEmpty()) {
            environment.answer("Чтобы добавить аббревиатуру добавьте к команде /add аббревиатуру и через пробел описание:\n`/add QR Quick Response`\n\nНе забывайте удалять за собой ненужные аббревиатуры командой /delete\nДопускается использование Markdown разметки в описании /markdown")
            return
        }

        if (description.isNullOrEmpty()) {
            environment.answer("Для добавления аббревиатуры добавьте еще описание через пробел: `/add аббревиатура описание`")
            return
        }

        val addResult = acronymService.onAddAcronym(environment.getUserData(), acronym, description)
        when (addResult) {
            AddAcronymResult.NotAuthorized -> environment.answer("Такая аббревиатура уже добавлена, и у вас нет прав перезаписать ее")
            is AddAcronymResult.AcronymCreated -> environment.answer("Добавлено ${addResult.acronym.value}")
            is AddAcronymResult.AcronymUpdated -> environment.answer("Обновлено ${addResult.acronym.value}")
        }
    }

    private suspend fun onRemoveAcronymCommand(environment: TextHandlerEnvironment, acronym: String?) {
        trackService.trackIncoming(environment.message.chat.id, "Remove", environment.text)
        if (acronym.isNullOrBlank()) {
            environment.answer("Для удаления добавьте название аббревиатуры: `/delete ABC`")
        } else {
            val result = acronymService.onRemoveAcronym(environment.getUserData(), acronym)
            when (result) {
                DeleteAcronymResult.Success -> environment.answer("Запись удалена")
                DeleteAcronymResult.NotFound -> environment.answer("Аббревиатура не найдена")
                DeleteAcronymResult.NotAuthorized -> environment.answer("Вы не можете удалять чужие аббревиатуры")
            }
        }
    }

    private fun onReport(environment: TextHandlerEnvironment, command: UserCommand.ReportCommand) {
        trackService.trackIncoming(environment.message.chat.id, "Report", "/report")
        val response = reportService.onReport(environment.getUserData(), command)
        when (response) {
            ReportResult.ShowHelp -> environment.answer(
                "Напишите сообщение разработчикам, предложите свои идеи, " +
                        "пожалуйтесь на аббревиатуру или работу бота в целом. Используйте команду /report <текст обращения> "
            )
            ReportResult.Error -> environment.answer("Произошла внутренняя ошибка. Обращение отправлено :(")
            is ReportResult.SendSupportMessage -> {
                environment.bot.sendMessage(
                    chatId = ChatId.fromId(response.supportChatId),
                    text = response.msg,
                )
                environment.answer("Обращение отправлено")
            }
        }
    }

    private fun onSupportChatMessage(env: TextHandlerEnvironment) {
        val result = reportService.onSupportChatResponse(env.getUserData(), env.message.replyToMessage, env.text)
        when (result) {
            is SupportChatResult.Error -> env.reply(text = result.text)
            is SupportChatResult.SendMessage ->
                env.bot.sendMessage(
                    chatId = ChatId.fromId(result.userId),
                    text = result.text,
                )
            is SupportChatResult.None -> Unit
        }
    }

    private fun onUnknownCommand(environment: TextHandlerEnvironment) {
        trackService.trackIncoming(environment.message.chat.id, "Unknown", environment.text)
        environment.answer("Некорректная команда")
    }

    private fun TextHandlerEnvironment.shouldBeIgnored(): Boolean {
        if (message.viaBot != null) {
            return true
        }

        return message.chat.type != PRIVATE_CHAT_TYPE && !isSupportChatMessage()
    }

    private fun TextHandlerEnvironment.isSupportChatMessage(): Boolean = message.chat.id == launchProperties.supportChatId

    private companion object {
        const val PRIVATE_CHAT_TYPE = "private"

        val LOGGER: Logger = LoggerFactory.getLogger(GeneralMessageHandler::class.java.canonicalName)
    }
}
