package com.municipality.backend.rest.user

import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.application.user.RegisterInternalUserCommand
import com.municipality.backend.application.user.UnencryptedPassword
import com.municipality.backend.application.user.UserAppService
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.municipality.district.DistrictId
import com.municipality.backend.domain.model.user.*
import com.municipality.backend.domain.model.user.role.DistrictAuditor
import com.municipality.backend.domain.model.user.role.DistrictResponsible
import com.municipality.backend.domain.model.user.role.MunicipalityAuditor
import com.municipality.backend.domain.model.user.role.MunicipalityResponsible
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/user")
class UserRestService(
    @Value("\${municipality.session.cookie}")
    val sessionCookieName: String,
    private val userAppService: UserAppService,
    private val loggedInUserResolver: LoggedInUserResolver
) {

    @PostMapping
    fun login(loginRequest: LoginRequest, httpServletResponse: HttpServletResponse): ResponseEntity<String> {
        val session = userAppService.login(Username(loginRequest.username),
            UnencryptedPassword(loginRequest.password))
        val cookie = Cookie(sessionCookieName, session.code)
        cookie.isHttpOnly = true
        cookie.path = "/"
        httpServletResponse.addCookie(cookie)
        return ResponseEntity.ok().build()
    }

    @PostMapping
    fun registerInternal(request: RegisterRequest): ResponseEntity<String> {
        val command = RegisterInternalUserCommand(loggedInUserResolver.loggedIn(), Username(request.username), Email(request.email),
        UnencryptedPassword(request.password), FirstName(request.firstName), LastName(request.lastName),
            request.isAdmin, request.municipalitiesResponsible.map { MunicipalityId(it) }.toSet(), request.municipalitiesAuditor.map { MunicipalityId(it) }.toSet(),
            request.districtsResponsible.map { DistrictId(it) }.toSet(), request.districtsAuditor.map { DistrictId(it) }.toSet())
        userAppService.register(command)
        return ResponseEntity.ok().build()
    }

    @PostMapping
    fun logout(httpServletResponse: HttpServletResponse): ResponseEntity<String> {
        val cookie = Cookie(sessionCookieName, null)
        cookie.isHttpOnly = true
        cookie.path = "/"
        cookie.maxAge = 0
        httpServletResponse.addCookie(cookie)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun me(): ResponseEntity<UserDto> {
        val loggedIn = loggedInUserResolver.loggedIn()
        if (loggedIn is RegisteredUser)
            return ResponseEntity.ok(toDto(loggedIn))

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    private fun toDto(user: RegisteredUser): UserDto {
        val municipalitiesResponsible = hashSetOf<String>()
        val municipalitiesAuditor = hashSetOf<String>()
        val districtsResponsible = hashSetOf<String>()
        val districtsAuditor = hashSetOf<String>()
        user.roles.all().forEach { role ->
            run {
                when (role) {
                    is MunicipalityResponsible -> municipalitiesResponsible.add(role.municipalityId.rawId.toString())
                    is MunicipalityAuditor -> municipalitiesAuditor.add(role.municipalityId.rawId.toString())
                    is DistrictResponsible -> districtsResponsible.add(role.districtId.rawId.toString())
                    is DistrictAuditor -> districtsAuditor.add(role.districtId.rawId.toString())
                }
            }
        }

        return UserDto(user.id.rawId.toString(), user.username.username!!, user.email.email!!, user.firstName.firstName!!,
            user.lastName.lastName!!, user.isAdmin(), municipalitiesResponsible, municipalitiesAuditor, districtsResponsible, districtsAuditor)
    }
}

data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val email: String, val password: String, val firstName: String, val lastName: String,
                           val isAdmin: Boolean, val municipalitiesResponsible: Set<String>, val municipalitiesAuditor: Set<String>,
                           val districtsResponsible: Set<String>, val districtsAuditor: Set<String>)
data class UserDto(val id: String, val username: String, val email: String,
                   val firstName: String, val lastName: String, val isAdmin: Boolean,
                   val municipalitiesResponsible: Set<String>, val municipalitiesAuditor: Set<String>,
                   val districtsResponsible: Set<String>, val districtsAuditor: Set<String>)
