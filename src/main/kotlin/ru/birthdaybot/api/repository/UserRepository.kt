package ru.birthdaybot.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.birthdaybot.model.entities.User

interface UserRepository : JpaRepository<User, Long> {
    @Query("select * from users d where TO_CHAR(d.birthday , 'MM-DD') =  :bdate", nativeQuery = true)
    fun findByBirthday(@Param("bdate") birthday: String): List<User>
}