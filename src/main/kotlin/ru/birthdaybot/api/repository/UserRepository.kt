package ru.birthdaybot.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import ru.birthdaybot.model.entities.User
import java.time.LocalDate

interface UserRepository : JpaRepository<User, Long> {
    fun findByBirthday(birthday : LocalDate) : List<User>
}