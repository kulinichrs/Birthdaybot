package ru.birthdaybot.model.dto

data class EventDto(
    val userIds: Collection<Long>,
    val message: String
)