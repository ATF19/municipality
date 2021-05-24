package com.municipality.backend.application.user

import com.municipality.backend.domain.model.user.CryptedPassword

interface Passwords {
    fun encrypt(unencryptedPassword: UnencryptedPassword): CryptedPassword
    fun areEquals(cryptedPassword: CryptedPassword, unencryptedPassword: UnencryptedPassword): Boolean
}