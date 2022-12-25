package ru.birthdaybot.impl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.birthdaybot.api.repository.UserRepository
import ru.birthdaybot.api.service.UserService
import ru.birthdaybot.model.entities.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

@Service
class UserServiceImpl(@Autowired val repo : UserRepository) : UserService {

    @Transactional(readOnly = false)
    override fun introduce(id: Long, introduceString: String) {
        val str = introduceString.replace("  ", " ").split(" ")
        val date = LocalDate.parse(str.last(), DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val fio = str.subList(0, str.size - 1).stream().collect(Collectors.joining(" "))

        val user = repo.findById(id).orElse(User(id.toString(), date, emptyList(), fio))
        user.birtday = date
        user.fio = fio

        repo.save(user)
    }
}
