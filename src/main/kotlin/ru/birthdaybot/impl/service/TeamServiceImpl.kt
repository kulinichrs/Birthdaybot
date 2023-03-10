package ru.birthdaybot.impl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.birthdaybot.api.repository.TeamRepository
import ru.birthdaybot.api.repository.UserRepository
import ru.birthdaybot.api.service.TeamService
import ru.birthdaybot.excepion.ItemAlreadyExistsException
import ru.birthdaybot.excepion.ItemNotFoundException
import ru.birthdaybot.model.dto.TeamDto
import ru.birthdaybot.model.dto.UserInfo
import ru.birthdaybot.model.entities.Team
import ru.birthdaybot.model.entities.User
import javax.transaction.Transactional

@Service
class TeamServiceImpl(
    @Autowired val teamRepository: TeamRepository,
    @Autowired val userRepository: UserRepository
) : TeamService {

    override fun createTeam(teamInfo: TeamDto) {
        teamRepository.findTeamByTeamName(teamInfo.teamName)
            ?.let { throw ItemAlreadyExistsException("Команда с таким именем уже существует") }
        teamRepository.save(
            Team(
                name = teamInfo.teamName,
                defaultText = teamInfo.credentials,
                description = teamInfo.description
            )
        )
    }

    @Transactional
    override fun updateTeam(teamInfo: TeamDto) {
        teamRepository.findTeamByTeamName(teamInfo.teamName)?.let {
            with(it) {
                if (teamInfo.credentials != null) defaultText = teamInfo.credentials
                if (teamInfo.description != null) description = teamInfo.description
            }
        }
    }

    override fun joinTeam(teamName: String, userInfo: UserInfo) {
        val team = teamRepository.findTeamByTeamName(teamName)
        team ?: throw ItemNotFoundException("Такой команды не существует")
        val user = userRepository.findById(userInfo.chatId).orElseGet {
            User(
                id = userInfo.chatId,
                fio = "${userInfo.firstName}${if (userInfo.lastName != null) " ${userInfo.lastName}" else ""}"
            )
        }
        if (!team.users.contains(user)) {
            team.users.add(user)
            teamRepository.save(team)
        } else {
            throw ItemAlreadyExistsException("Пользователь уже состоит в команде ${team.name}")
        }
    }

    override fun leaveTeam(teamName: String, userInfo: UserInfo) {
        teamRepository.findTeamByTeamName(teamName)?.apply {
            val user = userRepository.findById(userInfo.chatId).get()
            users.remove(user)
            teamRepository.save(this)
        } ?: throw ItemNotFoundException("Такой команды не существует")
    }

    override fun deleteTeam(teamName: String) {
        teamRepository.findTeamByTeamName(teamName)?.let {
            teamRepository.delete(it)
        } ?: throw ItemAlreadyExistsException("Такой команды не существует")
    }
}