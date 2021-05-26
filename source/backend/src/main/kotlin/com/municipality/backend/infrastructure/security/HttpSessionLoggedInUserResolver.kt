package com.municipality.backend.infrastructure.security

import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.user.AnonymousUser
import com.municipality.backend.domain.model.user.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class HttpSessionLoggedInUserResolver : LoggedInUserResolver {

    override fun loggedIn(): User<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication is SessionAuthentication)
            return authentication.user

        return AnonymousUser()
    }
}