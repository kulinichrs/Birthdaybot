package ru.birthdaybot.excepion

class ItemNotFoundException(override val message: String) : RuntimeException() {
}