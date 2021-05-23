package com.municipality.backend.infrastructure.persistence.repository.user

import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.RegisteredUserId
import com.municipality.backend.domain.model.user.Username
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserJpaRepository : JpaRepository<RegisteredUser, RegisteredUserId> {
    fun findByUsername(username: Username): Optional<RegisteredUser>
    fun existsByUsername(username: Username): Boolean
    fun existsByEmail(email: Email): Boolean
}