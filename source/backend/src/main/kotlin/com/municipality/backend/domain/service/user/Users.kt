package com.municipality.backend.domain.service.user

import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.RegisteredUserId
import com.municipality.backend.domain.model.user.Username
import java.util.*

interface Users {
    fun register(user: RegisteredUser)
    fun all(page: PageNumber): List<RegisteredUser>
    fun by(userId: RegisteredUserId): RegisteredUser
    fun by(username: Username): Optional<RegisteredUser>
    fun exists(username: Username): Boolean
    fun exists(email: Email): Boolean
    fun update(user: RegisteredUser)
}