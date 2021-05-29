package com.municipality.backend.rest.user

import com.municipality.backend.application.user.*
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.FIRST_PAGE
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.user.*
import com.municipality.backend.domain.model.user.role.DistrictAuditor
import com.municipality.backend.domain.model.user.role.DistrictResponsible
import com.municipality.backend.domain.model.user.role.MunicipalityAuditor
import com.municipality.backend.domain.model.user.role.MunicipalityResponsible
import com.municipality.backend.rest.core.PageDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/user")
@Transactional
class UserRestService(
    @Value("\${municipality.session.cookie}")
    val sessionCookieName: String,
    private val userAppService: UserAppService,
    private val loggedInUserResolver: LoggedInUserResolver
) {

    @PostMapping
    fun login(@RequestBody loginRequest: LoginRequest, httpServletResponse: HttpServletResponse): ResponseEntity<String> {
        val session = userAppService.login(Username(loginRequest.username),
            UnencryptedPassword(loginRequest.password))
        val cookie = Cookie(sessionCookieName, session.code)
        cookie.isHttpOnly = true
        cookie.path = "/"
        httpServletResponse.addCookie(cookie)
        return ResponseEntity.ok().build()
    }

    @PutMapping
    fun registerInternal(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        val command = RegisterInternalUserCommand(loggedInUserResolver.loggedIn(), Username(request.username), Email(request.email),
        UnencryptedPassword(request.password), FirstName(request.firstName), LastName(request.lastName),
            request.isAdmin)
        userAppService.register(command)
        return ResponseEntity.ok().build()
    }

    @PostMapping("logout")
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
        return ResponseEntity.ok(toDto(userAppService.profile(loggedIn)))
    }

    @PostMapping("update")
    fun updateProfile(@RequestBody request: UpdateProfileRequest): ResponseEntity<String> {
        var password: Optional<UnencryptedPassword> = Optional.empty()
        if (request.password != null)
            password = Optional.of(UnencryptedPassword(request.password))
        val command = UpdateProfileCommand(loggedInUserResolver.loggedIn(), Email(request.email), password, FirstName(request.firstName), LastName(request.lastName))
        userAppService.updateProfile(command)
        return ResponseEntity.ok().build()
    }

    @GetMapping("all")
    fun all(@RequestParam("page") page: Int?): ResponseEntity<PageDto<RegisteredUser, UserDto>> {
        val all = userAppService.all(loggedInUserResolver.loggedIn(), if (page == null) FIRST_PAGE else PageNumber(page), DEFAULT_PAGE_SIZE)
        return ResponseEntity.ok(PageDto(all, ::toDto))
    }

    @PostMapping("update-internal")
    fun updateInternalUser(@RequestBody request: UpdateInternalUserRequest): ResponseEntity<String> {
        var password: Optional<UnencryptedPassword> = Optional.empty()
        if (request.password != null)
            password = Optional.of(UnencryptedPassword(request.password))
        val command = UpdateInternalUserCommand(loggedInUserResolver.loggedIn(), Email(request.email), password,
            FirstName(request.firstName), LastName(request.lastName), request.isAdmin)
        userAppService.updateInternalUser(command)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("{userId}")
    fun delete(@PathVariable("userId") registeredUserId: String): ResponseEntity<String> {
        userAppService.deleteUser(DeleteUserCommand(loggedInUserResolver.loggedIn(), RegisteredUserId(registeredUserId)))
        return ResponseEntity.ok().build()
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
                           val isAdmin: Boolean)
data class UserDto(val id: String, val username: String, val email: String,
                   val firstName: String, val lastName: String, val isAdmin: Boolean,
                   val municipalitiesResponsible: Set<String>, val municipalitiesAuditor: Set<String>,
                   val districtsResponsible: Set<String>, val districtsAuditor: Set<String>)
data class UpdateProfileRequest(val email: String, val password: String?, val firstName: String, val lastName: String)
data class UpdateInternalUserRequest(val email: String, val password: String?, val firstName: String, val lastName: String, val isAdmin: Boolean)
