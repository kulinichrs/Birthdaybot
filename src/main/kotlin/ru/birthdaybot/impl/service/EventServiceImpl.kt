package ru.birthdaybot.impl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.birthdaybot.api.repository.UserRepository
import ru.birthdaybot.api.service.EventService
import ru.birthdaybot.model.dto.EventDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class EventServiceImpl(@Autowired val userRepo: UserRepository) : EventService {

    override fun getEvents(onDate: LocalDate): Collection<EventDto> {
        val bdate: String = onDate.format(DateTimeFormatter.ofPattern("MM-dd"))
        return userRepo.findByBirthday(bdate).flatMap { it.teams }.distinct().map { team ->
            val message = team.users
                .filter {
                    it.birthday?.month == onDate.month
                            && it.birthday?.dayOfMonth == onDate.dayOfMonth
                }
                .joinToString(
                    separator = ", ",
                    postfix = "Деньги скидывать сюда -> ${team.defaultText} , будем отправлять через 2 дня"
                ) {
                    "${onDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} день рождения у ${it.fio} !!!\n"
                }

            val ids = team.users.filter {
                it.birthday?.month != onDate.month
                        || it.birthday?.dayOfMonth != onDate.dayOfMonth
            }
                .map { it.id!! }
            EventDto(ids, message)
        }
    }

}