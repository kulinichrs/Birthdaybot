package ru.birthdaybot.impl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.birthdaybot.api.service.TeamService
import ru.birthdaybot.api.service.UserService
import ru.birthdaybot.excepion.ItemAlreadyExistsException
import ru.birthdaybot.excepion.ItemNotFoundException
import ru.birthdaybot.excepion.WrongNumberOfArgumentsException
import ru.birthdaybot.model.dto.TeamDto
import ru.birthdaybot.model.dto.UserInfo
import java.time.format.DateTimeParseException

interface Command {
    fun execute(args: List<String>, userInfo: UserInfo): String
}

abstract class AbstractCommand : Command {
    abstract val commandParam: BotCommand

    override fun execute(args: List<String>, userInfo: UserInfo): String {
        return try {
            if (args.size < commandParam.numberOfArg) throw WrongNumberOfArgumentsException("Неверное количество аргументов. Формат команды: [${commandParam.commandText}]")
            invoke(args, userInfo)
            commandParam.successText
        } catch (e: Exception) {
            when (e) {
                is WrongNumberOfArgumentsException -> e.message
                is ItemNotFoundException -> e.message
                is ItemAlreadyExistsException -> e.message
                is DateTimeParseException -> {
                    e.printStackTrace()
                    "Неверный формат даты. Корректный формат: dd.MM.yyyy"
                }

                is UnsupportedOperationException -> "Я такого ещё не умею"
                else -> {
                    e.printStackTrace()
                    "Inner error"
                }
            }
        }
    }

    abstract fun invoke(args: List<String>, userInfo: UserInfo)
}


@Component("/createteam")
class CreateTeamCommand(@Autowired val teamService: TeamService) : AbstractCommand() {
    override val commandParam = BotCommand.CREATE_TEAM

    override fun invoke(args: List<String>, userInfo: UserInfo) {
        teamService.createTeam(TeamDto(args[0], args[1]))
    }
}

@Component("/jointeam")
class JoinTeamCommand(@Autowired val teamService: TeamService) : AbstractCommand() {
    override val commandParam = BotCommand.JOIN_TEAM

    override fun invoke(args: List<String>, userInfo: UserInfo) {
        teamService.joinTeam(args[0], userInfo)
    }
}

@Component("/updateteam")
class UpdateTeamCommand(@Autowired val teamService: TeamService) : AbstractCommand() {
    override val commandParam = BotCommand.UPDATE_TEAM

    override fun invoke(args: List<String>, userInfo: UserInfo) {
        teamService.updateTeam(
            TeamDto(
                args[0],
                if (args.size >= 2) args[1] else null,
                if (args.size >= 3) args[2] else null
            )
        )
    }
}

@Component("/introduce")
class IntroduceCommand(@Autowired val userService: UserService) : AbstractCommand() {
    override val commandParam = BotCommand.INTRODUCE

    override fun invoke(args: List<String>, userInfo: UserInfo) {
        userService.introduce(userInfo.chatId, args)
    }
}

@Component("/deleteteam")
class DeleteTeamCommand(@Autowired val teamService: TeamService) : AbstractCommand() {
    override val commandParam = BotCommand.DELETE_TEAM

    override fun invoke(args: List<String>, userInfo: UserInfo) {
        teamService.deleteTeam(args[0])
    }
}

@Component("/leaveteam")
class LeaveTeamCommand(@Autowired val teamService: TeamService) : AbstractCommand() {
    override val commandParam = BotCommand.LEAVE_TEAM

    override fun invoke(args: List<String>, userInfo: UserInfo) {
        teamService.leaveTeam(args.first(), userInfo)
    }
}


@Component("/help")
class HelpCommand : Command {
    override fun execute(args: List<String>, userInfo: UserInfo): String {
        return BotCommand.values()
            .joinToString(separator = ",\n", prefix = "Список команд: \n") { "${it.commandText}  - ${it.description}" }
    }
}

@Component("/start")
class StartCommand : Command {
    override fun execute(args: List<String>, userInfo: UserInfo): String {
        return BotCommand.values().joinToString(
            separator = ",\n",
            prefix = "Привет! Вот что я умею: \n"
        ) { "${it.commandText}  - ${it.description}" }
    }
}

enum class BotCommand(
    val numberOfArg: Int,
    val description: String = "",
    val commandText: String,
    val successText: String
) {
    CREATE_TEAM(
        2,
        "Создать группу ",
        "/createteam teamName credentinals",
        "Группа успешно создана"
    ),
    JOIN_TEAM(
        1,
        "Присоединиться к группе",
        "/jointeam teamName",
        "Пользователь успешно добавлен в группу"
    ),
    UPDATE_TEAM(
        1,
        "Обновить инфо по группе",
        "/updateteam teamName [credentials] [description]",
        "Группа успешно отредактирована"
    ),
    INTRODUCE(
        2,
        "Добавить сведения о пользователе. Формат даты: dd.MM.yyyy",
        "/introduce name birthday",
        "Сведения добавлены"
    ),
    DELETE_TEAM(
        1,
        "Удалить группу",
        "/deleteteam name",
        "Группа удалена"
    ),
    LEAVE_TEAM(
        1,
        "Покинуть группу",
        "/leaveteam teamName",
        "Вы вышли из группы"
    ),
    HELP(
        0,
        "Помощь",
        "/help",
        ""
    )
}