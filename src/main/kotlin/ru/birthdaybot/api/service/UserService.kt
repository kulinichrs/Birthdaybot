package ru.birthdaybot.api.service

interface UserService {
    /**
     * представиться, в строке д.б. ФИО и дата рождения через пробелы
     */
    fun introduce(id: Long, introduceString : String);
}