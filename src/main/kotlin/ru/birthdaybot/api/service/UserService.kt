package ru.birthdaybot.api.service

interface UserService {
    /**
     * Представиться, в строке д.б. ФИО и дата рождения через пробелы
     */
    fun introduce(id: Long, args: List<String>)
}