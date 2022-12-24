package ru.birthdaybot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BirthdayBotApplication

fun main(args: Array<String>) {
    runApplication<BirthdayBotApplication>(*args)
}
