package ru.birthdaybot.impl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.birthdaybot.api.repository.TeamRepository
import ru.birthdaybot.api.repository.UserRepository
import ru.birthdaybot.api.service.TeamService
import ru.birthdaybot.excepion.ItemNotFoundException
import ru.birthdaybot.model.dto.TeamDto
import ru.birthdaybot.model.dto.UserInfo
import ru.birthdaybot.model.entities.Team
import ru.birthdaybot.model.entities.User

@Service
class TeamServiceImpl(
    @Autowired val teamRepository: TeamRepository,
    @Autowired val userRepository: UserRepository
) : TeamService {

    override fun createTeam(teamInfo: TeamDto) {
        teamRepository.save(
            Team(
                name = teamInfo.teamName,
                defaultText = teamInfo.credentials,
                description = teamInfo.description
            )
        )
    }

    override fun joinTeam(teamInfo: TeamDto, userInfo: UserInfo) {
        teamRepository.findTeamByTeamName(teamInfo.teamName)?.apply {
            val user = userRepository.findById(userInfo.chatId).orElseGet {
                User(
                    id = userInfo.chatId,
                    birthday = userInfo.birthday,
                    fio = "${userInfo.firstName} ${userInfo.lastName}"
                )
            }
            users?.add(user)
            teamRepository.save(this)
        } ?: throw ItemNotFoundException("Team is not found")
    }
}