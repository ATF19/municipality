package com.municipality.backend.application.user

import com.municipality.backend.domain.model.user.User

interface LoggedInUserResolver {
    fun loggedIn(): User<*>
}