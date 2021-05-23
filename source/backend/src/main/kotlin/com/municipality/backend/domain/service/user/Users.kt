package com.municipality.backend.domain.service.user

import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.Username

interface Users {
    fun register(user: RegisteredUser)
    fun all(page: PageNumber): List<RegisteredUser>
    fun by(username: Username): RegisteredUser
    fun exists(username: Username): Boolean
    fun exists(email: Email): Boolean
}