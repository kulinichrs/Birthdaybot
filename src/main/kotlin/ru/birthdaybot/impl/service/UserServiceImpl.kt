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
class UserServiceImpl(@Autowired val repo: UserRepository) : UserService {

    @Transactional(readOnly = false)
    override fun introduce(id: Long, args: List<String>) {
        val date = LocalDate.parse(args[args.size - 1], DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val fio = args.subList(0, args.size - 1).stream().collect(Collectors.joining(" "))

        val user = repo.findById(id).orElse(User(id, date, fio = fio))
        user.birthday = date
        user.fio = fio

        repo.save(user)
    }
}
