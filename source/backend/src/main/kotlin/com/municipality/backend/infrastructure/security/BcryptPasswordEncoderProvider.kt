package com.municipality.backend.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class BcryptPasswordEncoderProvider {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}