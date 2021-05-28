package com.municipality.backend.rest.user

import com.municipality.backend.application.user.*
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.municipality.district.DistrictId
import com.municipality.backend.domain.model.user.RegisteredUserBuilder
import com.municipality.backend.domain.model.user.Username
import com.municipality.backend.shared_code_for_tests.LoggedInUserForTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
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
        val municipalityId1 = MunicipalityId()
        val municipalityId2 = MunicipalityId()
        val municipalityId3 = MunicipalityId()
        val districtId1 = DistrictId()
        val districtId2 = DistrictId()
        val registerRequest = RegisterRequest("atef", "demo@test.com", "myStrongPass",
        "John", "Doe", true, setOf(municipalityId1.rawId.toString(), municipalityId2.rawId.toString()),
            setOf(municipalityId3.rawId.toString()), setOf(districtId1.rawId.toString(), districtId2.rawId.toString()), emptySet())

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
        assertThat(result.municipalitiesResponsibleFor).containsExactlyInAnyOrder(municipalityId1, municipalityId2)
        assertThat(result.municipalitiesAuditorFor).containsOnly(municipalityId3)
        assertThat(result.districtsResponsibleFor).containsExactlyInAnyOrder(districtId1, districtId2)
        assertThat(result.districtsAuditorFor).isEmpty()
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
}