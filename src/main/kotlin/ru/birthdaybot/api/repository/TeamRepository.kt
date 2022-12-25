package ru.birthdaybot.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.birthdaybot.model.entities.Team

interface TeamRepository : JpaRepository<Team, Long> {

    @Query("select t from Team t where t.name = :name")
    fun findTeamByTeamName(name: String): Team?
}