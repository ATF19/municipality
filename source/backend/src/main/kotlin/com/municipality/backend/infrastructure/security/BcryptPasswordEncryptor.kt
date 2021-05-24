package com.municipality.backend.infrastructure.security

import com.municipality.backend.application.user.Passwords
import com.municipality.backend.application.user.UnencryptedPassword
import com.municipality.backend.domain.model.user.CryptedPassword
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class BcryptPasswordEncryptor(private val passwordEncoder: PasswordEncoder) : Passwords {

    override fun encrypt(unencryptedPassword: UnencryptedPassword) =
        CryptedPassword(passwordEncoder.encode(unencryptedPassword.password))

    override fun areEquals(cryptedPassword: CryptedPassword, unencryptedPassword: UnencryptedPassword) =
        passwordEncoder.matches(unencryptedPassword.password, cryptedPassword.password)
}