package ru.birthdaybot.excepion

import java.lang.RuntimeException

class ItemNotFoundException(override val message: String?): RuntimeException() {
}