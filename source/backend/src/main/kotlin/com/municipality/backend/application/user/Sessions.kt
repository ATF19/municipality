package com.municipality.backend.application.user

import com.municipality.backend.domain.model.user.RegisteredUser

interface Sessions {
    fun create(user: RegisteredUser): Session
    fun loggedInUser(session: Session): RegisteredUser
}