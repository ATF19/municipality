package com.municipality.backend.infrastructure.security

import com.municipality.backend.application.user.Session
import com.municipality.backend.application.user.Sessions
import com.municipality.backend.domain.model.user.AnonymousUser
import com.municipality.backend.domain.model.user.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.WebUtils
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SessionFilter(
    @Value("\${municipality.session.cookie}")
    val sessionCookieName: String,
    private val sessions: Sessions
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val sessionCookie = WebUtils.getCookie(request, sessionCookieName)
        var user: User<*> = AnonymousUser()
        if (sessionCookie != null && sessionCookie.value.isNotEmpty())
            user = sessions.loggedInUser(Session(sessionCookie.value))

        SecurityContextHolder.getContext().authentication = SessionAuthentication(user)
        chain.doFilter(request, response)
    }
}

class SessionAuthentication(val user: User<*>) : Authentication {

    override fun getName() = user.name()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun getCredentials() = user.id().toString()

    override fun getDetails() = user.id().toString()

    override fun getPrincipal() = user.id().toString()

    override fun isAuthenticated() = true

    override fun setAuthenticated(p0: Boolean) {}

}