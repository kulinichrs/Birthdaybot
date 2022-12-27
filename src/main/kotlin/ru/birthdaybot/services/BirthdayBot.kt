package ru.birthdaybot.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import ru.birthdaybot.api.service.EventService
import ru.birthdaybot.api.service.TeamService
import ru.birthdaybot.excepion.ItemAlreadyExistsException
import ru.birthdaybot.excepion.ItemNotFoundException
import ru.birthdaybot.model.dto.TeamDto
import ru.birthdaybot.model.dto.UserInfo
import java.time.LocalDate

@EnableScheduling
@Service
class BirthdayBot : TelegramLongPollingBot() {
    @Value("\${telegram.botName}")
    private val botName: String = ""

    @Value("\${telegram.token}")
    private val token: String = ""

    @Autowired
    lateinit var teamService: TeamService


    @Value("\${notification.daysbefore}")
    private val daysBefore : Long = 3

    @Autowired
    lateinit var birthdayService: EventService


    override fun getBotToken(): String = token

    override fun getBotUsername(): String = botName

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            val message = update.message
            val chatId = message.chatId
            val buttons: MutableList<String> = mutableListOf("Главное меню")
            val responseText = if (message.hasText()) {
                val messageText = message.text
                when  {
                        messageText.contains("/createteam", true) -> {
                           createTeam(messageText)
                        }
                        messageText.contains("/hello", true) -> {
                            "hello bro"
                        }
                        messageText.contains("/ny", true) -> {
                            "Happy New Year!"
                        }
                        messageText.contains("/jointeam", true) -> {
                            joinTeam(messageText, UserInfo(message))
                        }
                        messageText.contains("/updateteam", true) -> {
                            updateTeam(messageText)
                        }
                        messageText=="/start" || messageText== "Главное меню" -> {
                            buttons.add("Представиться")
                            " "
                        }
                        else -> {
                            " "
                        }
                    }
            } else {
                "text"
            }
            sendNotification(chatId, responseText, buttons)
        }
    }

    private fun updateTeam(messageText: String): String {
        return try {
            val teamInfo = messageText.split(" ")
            teamService.updateTeam(TeamDto(teamInfo[1], if (teamInfo.size >= 3) teamInfo[2] else null))
            "Команда с именем ${teamInfo[1]} успешно изменена"
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is IndexOutOfBoundsException -> "Не задано имя команды или текст для команды"
                else -> "Внутренняя ошибка"
            }
        }
    }

    private fun createTeam(messageText: String): String {
        return try {
            val teamInfo = messageText.split(" ")
            teamService.createTeam(TeamDto(teamInfo[1], teamInfo[2]))
            "Команда с именем ${teamInfo[1]} успешно создана"
        } catch (e: Exception) {
            //todo поменять на логер
            e.printStackTrace()
            when (e) {
                is IndexOutOfBoundsException -> "Не задано имя команды или текст для команды"
                is ItemAlreadyExistsException -> e.message
                else -> "Внутренняя ошибка"
            }
        }
    }

    private fun joinTeam(messageText: String, userInfo: UserInfo): String {
        return try {
            val teamInfo = messageText.split(" ")
            teamService.joinTeam(teamInfo[1], userInfo)
            "Пользователь добавлен в команду '${teamInfo[1]}'"
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is ItemNotFoundException -> e.message
                is IndexOutOfBoundsException -> "Не задано имя команды"
                else -> "Внутренняя ошибка"
            }
        }
    }


    private fun sendNotification(chatId: Long, responseText: String, buttons: List<String>) {
        val responseMessage = SendMessage(chatId.toString(), responseText)
        responseMessage.enableMarkdown(true)
//        responseMessage.replyMarkup = getReplyMarkup(buttons)

        execute(responseMessage)
    }

    private fun getReplyMarkup(allButtons: List<String>): ReplyKeyboardMarkup {
        val markup = ReplyKeyboardMarkup()
        markup.keyboard = allButtons.map { rowButton ->
            val row = KeyboardRow()
            row.add(rowButton)
            row
        }
        return markup
    }

    @Scheduled(cron = "\${telegram.timenotification}")
    private fun notifyUsersBySchedule() {
        val searchDate = LocalDate.now().plusDays(daysBefore)
        birthdayService.getEvents(searchDate).forEach{ event ->
            event.userIds.forEach{userId ->
                sendNotification(userId, event.message, emptyList())
            }
        }
    }

}