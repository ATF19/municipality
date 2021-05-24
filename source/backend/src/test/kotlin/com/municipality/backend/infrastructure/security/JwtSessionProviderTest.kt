package com.municipality.backend.infrastructure.security

import com.municipality.backend.application.user.Session
import com.municipality.backend.application.user.Sessions
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.user.RegisteredUserBuilder
import com.municipality.backend.domain.model.user.RegisteredUserId
import com.municipality.backend.domain.service.user.Users
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.testng.annotations.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class JwtSessionProviderTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var sessions: Sessions

    @Autowired
    private lateinit var users: Users

    @Value("\${municipality.session.secret}")
    private lateinit var jwtSecret: String

    @Test(groups = [TestGroup.INTEGRATION])
    fun generate_session_for_user() {
        // given
        val user = RegisteredUserBuilder().build()

        // when
        val session = sessions.create(user)

        // then
        assertThat(session.code).isNotEmpty
        val tokenBody = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(session.code).body
        assertThat(tokenBody.subject).isEqualTo(user.id().rawId.toString())
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun throw_exception_if_got_empty_session() {
        // given
        val session = Session("")
        // when

        // then
        assertThatThrownBy { sessions.loggedInUser(session) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun throw_exception_if_got_wrong_session() {
        // given
        val session = Session("thisiswrong")

        // when

        // then
        assertThatThrownBy { sessions.loggedInUser(session) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun throw_exception_if_got_expired_session() {
        // given
        val token = Jwts.builder()
            .setSubject(RegisteredUserId().rawId.toString())
            .setIssuedAt(localDateToDate(LocalDate.of(2020, 11, 12)))
            .setExpiration(localDateToDate(LocalDate.of(2020, 11, 15)))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
        val session = Session(token)

        // when

        // then
        assertThatThrownBy { sessions.loggedInUser(session) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun get_logged_in_user() {
        // given
        val user = RegisteredUserBuilder().build()
        users.register(user)
        val session = sessions.create(user)

        // when
        val loggedInUser = sessions.loggedInUser(session)

        // then
        assertThat(loggedInUser).isEqualTo(user)
    }

    private fun localDateToDate(localDate: LocalDate) = Date
        .from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
}