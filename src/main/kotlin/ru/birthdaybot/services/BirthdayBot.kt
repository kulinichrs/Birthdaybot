package ru.birthdaybot.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.birthdaybot.api.service.EventService
import ru.birthdaybot.impl.service.Command
import ru.birthdaybot.model.dto.UserInfo
import java.time.LocalDate

@EnableScheduling
@Service
class BirthdayBot : TelegramLongPollingBot() {
    @Value("\${telegram.botName}")
    private val botName: String = ""

    @Value("\${telegram.token}")
    private val token: String = ""

    @Value("\${notification.daysbefore}")
    private val daysBefore: Long = 3

    @Autowired
    private lateinit var commands: Map<String, Command>

    @Autowired
    lateinit var birthdayService: EventService


    override fun getBotToken(): String = token

    override fun getBotUsername(): String = botName

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            val message = update.message
            if (message.hasText() && message.text.startsWith("/")) {
                val args = (message.text).split(" ").filter { it.isNotBlank() }
                val result = (commands[args[0].lowercase()])?.execute(args.subList(1, args.size), UserInfo(message))
                    ?: "Возможно, вы ввели неправильную команду. Используйте /help для помощи"
                sendNotification(message.chatId, result, emptyList())
            }

        }
    }

    private fun sendNotification(chatId: Long, responseText: String, buttons: List<String>) {
        val responseMessage = SendMessage(chatId.toString(), responseText)
        responseMessage.enableMarkdown(true)
//        responseMessage.replyMarkup = getReplyMarkup(buttons)
        execute(responseMessage)
    }

//    private fun getReplyMarkup(allButtons: List<String>): ReplyKeyboardMarkup {
//        val markup = ReplyKeyboardMarkup()
//        markup.keyboard = allButtons.map { rowButton ->
//            val row = KeyboardRow()
//            row.add(rowButton)
//            row
//        }
//        return markup
//    }

    @Scheduled(cron = "\${telegram.timenotification}")
    private fun notifyUsersBySchedule() {
        //TODO удалить
        println("Запустили")
        val searchDate = LocalDate.now().plusDays(daysBefore)
        birthdayService.getEvents(searchDate).forEach { event ->
            event.userIds.forEach { userId ->
                sendNotification(userId, event.message, emptyList())
            }
        }
    }

}