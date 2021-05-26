package com.municipality.backend.shared_code_for_tests

import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.user.RegisteredUserBuilder

class LoggedInUserForTest : LoggedInUserResolver {

    companion object {
        val user = RegisteredUserBuilder().build()
    }

    override fun loggedIn() = user
}