package com.municipality.backend.rest.user

import com.municipality.backend.application.user.*
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.FIRST_PAGE
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.user.RegisteredUserBuilder
import com.municipality.backend.domain.model.user.RegisteredUserId
import com.municipality.backend.domain.model.user.Username
import com.municipality.backend.shared_code_for_tests.LoggedInUserForTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.springframework.http.HttpStatus
import org.testng.annotations.Test
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

class UserRestServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun login_user() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        val loginRequest = LoginRequest("atef", "12345")
        val userAppService = mockk<UserAppService>()
        val httpServletResponse = mockk<HttpServletResponse>(relaxed = true)
        val cookieName = "TestCookie"
        val restService = UserRestService(cookieName, userAppService, loggedInUserResolver)
        val session = Session("test")
        every { userAppService.login(Username(loginRequest.username), UnencryptedPassword(loginRequest.password)) }.returns(session)

        // when
        val response = restService.login(loginRequest, httpServletResponse)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val captureSlot = slot<Cookie>()
        verify { httpServletResponse.addCookie(capture(captureSlot)) }
        val cookie = captureSlot.captured
        assertThat(cookie.name).isEqualTo(cookieName)
        assertThat(cookie.value).isEqualTo(session.code)
        assertThat(cookie.isHttpOnly).isTrue
        assertThat(cookie.path).isEqualTo("/")
    }

    @Test(groups = [TestGroup.UNIT])
    fun logout_user() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        val userAppService = mockk<UserAppService>()
        val httpServletResponse = mockk<HttpServletResponse>(relaxed = true)
        val cookieName = "TestCookie"
        val restService = UserRestService(cookieName, userAppService, loggedInUserResolver)

        // when
        val response = restService.logout(httpServletResponse)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val captureSlot = slot<Cookie>()
        verify { httpServletResponse.addCookie(capture(captureSlot)) }
        val cookie = captureSlot.captured
        assertThat(cookie.name).isEqualTo(cookieName)
        assertThat(cookie.value).isEqualTo(null)
        assertThat(cookie.maxAge).isEqualTo(0)
        assertThat(cookie.isHttpOnly).isTrue
        assertThat(cookie.path).isEqualTo("/")
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_logged_in_user() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        val userAppService = mockk<UserAppService>()
        val cookieName = "TestCookie"
        val restService = UserRestService(cookieName, userAppService, loggedInUserResolver)
        val registeredUser = RegisteredUserBuilder().build()
        every { loggedInUserResolver.loggedIn() }.returns(registeredUser)
        every { userAppService.profile(loggedInUserResolver.loggedIn()) }.returns(registeredUser)

        // when
        val response = restService.me()

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isInstanceOf(UserDto::class.java)
        assertThat(response.body!!.username).isEqualTo(registeredUser.username.username)
        assertThat(response.body!!.email).isEqualTo(registeredUser.email.email)
        assertThat(response.body!!.firstName).isEqualTo(registeredUser.firstName.firstName)
        assertThat(response.body!!.lastName).isEqualTo(registeredUser.lastName.lastName)
        assertThat(response.body!!.isAdmin).isEqualTo(registeredUser.isAdmin())
    }

    @Test(groups = [TestGroup.UNIT])
    fun register_user() {
        // given
        val userAppService = mockk<UserAppService>(relaxed = true)
        val cookieName = "TestCookie"
        val loggedInUserResolver = LoggedInUserForTest()
        val restService = UserRestService(cookieName, userAppService, loggedInUserResolver)
        val registerRequest = RegisterRequest("atef", "demo@test.com", "myStrongPass",
        "John", "Doe", true)

        // when
        val response = restService.registerInternal(registerRequest)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val captureSlot = slot<RegisterInternalUserCommand>()
        verify { userAppService.register(capture(captureSlot)) }
        val result = captureSlot.captured
        assertThat(result.user).isEqualTo(loggedInUserResolver.loggedIn())
        assertThat(result.username.username).isEqualTo(registerRequest.username)
        assertThat(result.email.email).isEqualTo(registerRequest.email)
        assertThat(result.unencryptedPassword.password).isEqualTo(registerRequest.password)
        assertThat(result.firstName.firstName).isEqualTo(registerRequest.firstName)
        assertThat(result.lastName.lastName).isEqualTo(registerRequest.lastName)
    }

    @Test(groups = [TestGroup.UNIT])
    fun update_profile() {
        // given
        val userAppService = mockk<UserAppService>(relaxed = true)
        val cookieName = "TestCookie"
        val loggedInUserResolver = LoggedInUserForTest()
        val restService = UserRestService(cookieName, userAppService, loggedInUserResolver)
        val request = UpdateProfileRequest("demo@test.com", "myStrongPass",
            "John", "Doe")

        // when
        val response = restService.updateProfile(request)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val captureSlot = slot<UpdateProfileCommand>()
        verify { userAppService.updateProfile(capture(captureSlot)) }
        val result = captureSlot.captured
        assertThat(result.user).isEqualTo(loggedInUserResolver.loggedIn())
        assertThat(result.email.email).isEqualTo(request.email)
        assertThat(result.unencryptedPassword).contains(UnencryptedPassword(request.password!!))
        assertThat(result.firstName.firstName).isEqualTo(request.firstName)
        assertThat(result.lastName.lastName).isEqualTo(request.lastName)
    }

    @Test(groups = [TestGroup.UNIT])
    fun update_profile_without_password() {
        // given
        val userAppService = mockk<UserAppService>(relaxed = true)
        val cookieName = "TestCookie"
        val loggedInUserResolver = LoggedInUserForTest()
        val restService = UserRestService(cookieName, userAppService, loggedInUserResolver)
        val request = UpdateProfileRequest("demo@test.com", null,
            "John", "Doe")

        // when
        val response = restService.updateProfile(request)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val captureSlot = slot<UpdateProfileCommand>()
        verify { userAppService.updateProfile(capture(captureSlot)) }
        val result = captureSlot.captured
        assertThat(result.unencryptedPassword).isEmpty
    }

    @Test(groups = [TestGroup.UNIT])
    fun update_internal_user() {
        // given
        val userAppService = mockk<UserAppService>(relaxed = true)
        val cookieName = "TestCookie"
        val loggedInUserResolver = LoggedInUserForTest()
        val restService = UserRestService(cookieName, userAppService, loggedInUserResolver)
        val request = UpdateInternalUserRequest("demo@test.com", "myStrongPass",
            "John", "Doe", true)

        // when
        val response = restService.updateInternalUser(request)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val captureSlot = slot<UpdateInternalUserCommand>()
        verify { userAppService.updateInternalUser(capture(captureSlot)) }
        val result = captureSlot.captured
        assertThat(result.user).isEqualTo(loggedInUserResolver.loggedIn())
        assertThat(result.email.email).isEqualTo(request.email)
        assertThat(result.unencryptedPassword).contains(UnencryptedPassword(request.password!!))
        assertThat(result.firstName.firstName).isEqualTo(request.firstName)
        assertThat(result.lastName.lastName).isEqualTo(request.lastName)
        assertThat(result.isAdmin).isEqualTo(request.isAdmin)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_all_users() {
        // given
        val userAppService = mockk<UserAppService>(relaxed = true)
        val cookieName = "TestCookie"
        val loggedInUserResolver = LoggedInUserForTest()
        val restService = UserRestService(cookieName, userAppService, loggedInUserResolver)
        val user1 = RegisteredUserBuilder().build()
        val user2 = RegisteredUserBuilder().build()
        val page = Page(listOf(user1, user2), FIRST_PAGE, DEFAULT_PAGE_SIZE, 20)
        every { userAppService.all(loggedInUserResolver.loggedIn(), FIRST_PAGE, DEFAULT_PAGE_SIZE) }.returns(page)

        // when
        val response = restService.all(null)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.number).isEqualTo(FIRST_PAGE.number)
        assertThat(response.body!!.size).isEqualTo(DEFAULT_PAGE_SIZE.size)
        assertThat(response.body!!.totalPages).isEqualTo(20)
        assertThat(response.body!!.elements)
            .extracting({dto -> dto.id})
            .containsExactlyInAnyOrder(Tuple.tuple(user1.id.rawId.toString()), Tuple.tuple(user2.id.rawId.toString()))
    }

    @Test(groups = [TestGroup.UNIT])
    fun delete_user() {
        // given
        val userAppService = mockk<UserAppService>(relaxed = true)
        val cookieName = "TestCookie"
        val loggedInUserResolver = LoggedInUserForTest()
        val restService = UserRestService(cookieName, userAppService, loggedInUserResolver)
        val registeredUserId = RegisteredUserId()

        // when
        val response = restService.delete(registeredUserId.rawId.toString())

        // then
        verify { userAppService.deleteUser(DeleteUserCommand(loggedInUserResolver.loggedIn(), registeredUserId)) }
    }
}