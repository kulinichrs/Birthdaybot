package ru.birthdaybot.api.service

import ru.birthdaybot.model.dto.EventDto
import java.time.LocalDate

/**
 * сервис получения списка рассылки сообщений на дату
 */
interface EventService {

    fun getEvents(onDate : LocalDate) : Collection<EventDto>
}