package com.municipality.backend.application.utility

import com.municipality.backend.application.user.UnencryptedPassword
import java.util.regex.Pattern

private val VALID_PASSWORD_ADDRESS_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$")

class PasswordUtility private constructor(){
    companion object {
        fun isStrong(unencryptedPassword: UnencryptedPassword): Boolean {
            val matcher = VALID_PASSWORD_ADDRESS_REGEX.matcher(unencryptedPassword.password)
            return matcher.matches()
        }
    }
}