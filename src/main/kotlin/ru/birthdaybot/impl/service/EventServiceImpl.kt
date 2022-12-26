package ru.birthdaybot.impl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.birthdaybot.api.repository.UserRepository
import ru.birthdaybot.api.service.EventService
import ru.birthdaybot.model.dto.EventDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

@Service
class EventServiceImpl(@Autowired val userRepo : UserRepository) : EventService {

    override fun getEvents(onDate : LocalDate) : Collection<EventDto> {
        return userRepo.findByBirthday(onDate).flatMap { it.teams }.distinct().map{
            val message = it.users.stream()
                .filter{ it.birthday == onDate }
                .map { it.fio }
                .collect(Collectors.joining(
                    ", ",
                    "${onDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}} день рождения у ",
                    "! ${it.defaultText}"))

            val ids = it.users.filter { it.birthday != onDate }.map { it.id!! }
            EventDto(ids, message)
        }
    }

}