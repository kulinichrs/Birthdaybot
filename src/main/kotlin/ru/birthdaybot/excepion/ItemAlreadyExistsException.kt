package ru.birthdaybot.excepion

import java.lang.RuntimeException

class ItemAlreadyExistsException(override val message: String): RuntimeException() {
}