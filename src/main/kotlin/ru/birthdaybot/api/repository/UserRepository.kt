package ru.birthdaybot.api.repository

import org.springframework.data.repository.CrudRepository
import ru.birthdaybot.model.entities.User

interface UserRepository : CrudRepository<User, Long> {
}