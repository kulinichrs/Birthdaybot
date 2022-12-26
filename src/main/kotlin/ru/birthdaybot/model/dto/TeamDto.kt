package ru.birthdaybot.model.dto

data class TeamDto(
    val teamName: String,
    val credentials: String?,
    val description: String? = null
)
