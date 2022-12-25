package ru.birthdaybot.model.dto

import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDate

class UserInfo(message: Message) {
    val chatId: Long = message.chatId
    val phone: String? = if (message.contact != null) {
        message.contact.phoneNumber
    } else {
        null
    }
    val userName: String = message.chat.userName
    val firstName: String? = message.chat.firstName
    val lastName: String? = message.chat.lastName
    val birthday: LocalDate? = null
}
