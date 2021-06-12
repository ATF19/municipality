package com.municipality.backend.infrastructure.security

import com.municipality.backend.application.user.Session
import com.municipality.backend.application.user.Sessions
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.RegisteredUserId
import com.municipality.backend.domain.service.user.Users
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtSessionProvider(
    @Value("\${municipality.session.issuer}")
    val issuer: String,

    @Value("\${municipality.session.ttlInSeconds}")
    val ttlInSeconds: Int,

    @Value("\${municipality.session.secret}")
    val jwtSecret: String,

    private val users: Users
) : Sessions {

    override fun create(user: RegisteredUser): Session {
        val currentTimeMillis = System.currentTimeMillis()
        val token = Jwts.builder()
            .setSubject(user.id().rawId.toString())
            .setIssuedAt(Date(currentTimeMillis))
            .setIssuer(issuer)
            .setExpiration(Date(currentTimeMillis + ttlInSeconds * 1000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
        return Session(token)
    }

    override fun loggedInUser(session: Session): RegisteredUser {
        if (session.code.isEmpty())
            throw InsufficientPermissionException()

        val parsedBody = parse(session)
        verifyNotExpired(parsedBody)
        val userId = getUserId(parsedBody.subject)
        return users.by(userId)
    }

    private fun getUserId(subject: String?): RegisteredUserId {
        if (subject == null)
            throw InsufficientPermissionException()

        try {
            return RegisteredUserId(UUID.fromString(subject))
        } catch (exception: IllegalArgumentException) {
            throw InsufficientPermissionException()
        }
    }

    private fun verifyNotExpired(parsedBody: Claims) {
        if (parsedBody.expiration.before(Date()))
            throw InsufficientPermissionException()
    }

    private fun parse(session: Session): Claims {
        try {
            return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(session.code)
                .body
        } catch (e: JwtException) {
            throw InsufficientPermissionException()
        }
    }
}