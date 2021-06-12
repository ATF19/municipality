package com.municipality.backend.application.utility

import com.municipality.backend.domain.model.user.Email
import java.util.regex.Pattern

private val VALID_EMAIL_ADDRESS_REGEX =
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

class EmailUtility private constructor() {
    companion object {
        fun isValid(email: Email): Boolean {
            if (email.email == null)
                return false
            val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email.email)
            return matcher.matches()
        }
    }
}