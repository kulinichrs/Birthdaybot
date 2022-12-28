package ru.birthdaybot.api.service

import ru.birthdaybot.model.dto.TeamDto
import ru.birthdaybot.model.dto.UserInfo

interface TeamService {
    fun createTeam(teamInfo: TeamDto)
    fun joinTeam(teamName: String, userInfo: UserInfo)
    fun updateTeam(teamInfo: TeamDto)
    fun deleteTeam(teamName: String)
}