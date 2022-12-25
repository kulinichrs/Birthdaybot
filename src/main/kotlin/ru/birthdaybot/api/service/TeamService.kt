package ru.birthdaybot.api.service

import ru.birthdaybot.model.dto.TeamDto
import ru.birthdaybot.model.dto.UserInfo

interface TeamService {
    fun createTeam(teamInfo: TeamDto)
    fun joinTeam(teamInfo: TeamDto, userInfo: UserInfo)
}