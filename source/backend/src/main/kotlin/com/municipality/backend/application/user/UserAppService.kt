package com.municipality.backend.application.user

import com.municipality.backend.application.utility.EmailUtility
import com.municipality.backend.application.utility.PasswordUtility
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.core.error.MunicipalityException
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.User
import com.municipality.backend.domain.model.user.Username
import com.municipality.backend.domain.model.user.role.*
import com.municipality.backend.domain.service.user.Users
import org.springframework.stereotype.Component


@Component
class UserAppService(
    private val users: Users,
    private val passwords: Passwords,
    private val sessions: Sessions
) {

    fun register(command: RegisterInternalUserCommand) {
        if (!command.user.isAdmin())
            throw InsufficientPermissionException()

        verifyNoMissingInformation(command)
        verifyPasswordIsNotWeak(command.unencryptedPassword)
        verifyEmailValidAndDoesNotExists(command.email)
        verifyUsernameDoesNotExists(command.username)

        val encryptedPassword = passwords.encrypt(command.unencryptedPassword)
        val user = RegisteredUser()
        user.email = command.email
        user.username = command.username
        user.cryptedPassword = encryptedPassword
        user.firstName = command.firstName
        user.lastName = command.lastName
        if (command.isAdmin)
            user.roles.grant(Admin())
        command.municipalitiesResponsibleFor.forEach { user.roles.grant(MunicipalityResponsible(it)) }
        command.municipalitiesAuditorFor.forEach { user.roles.grant(MunicipalityAuditor(it)) }
        command.districtsResponsibleFor.forEach { user.roles.grant(DistrictResponsible(it)) }
        command.districtsAuditorFor.forEach { user.roles.grant(DistrictAuditor(it)) }
        users.register(user)
    }

    fun login(username: Username, password: UnencryptedPassword): Session {
        if (username.username == null)
            throw IncorrectUsernameOrPasswordException()

        val user = users.by(username).orElseThrow { IncorrectUsernameOrPasswordException() }
        if (!passwords.areEquals(user.cryptedPassword, password))
            throw IncorrectUsernameOrPasswordException()

        return sessions.create(user)
    }

    fun profile(user: User<*>): RegisteredUser {
        if (!user.isRegistered() || user !is RegisteredUser)
            throw InsufficientPermissionException()

        return user
    }

    fun updateProfile(command: UpdateProfileCommand) {
        if (!command.user.isRegistered())
            throw InsufficientPermissionException()

        verifyMissingUpdateInformation(command)
        val user = command.user as RegisteredUser
        if (user.email != command.email)
            verifyEmailValidAndDoesNotExists(command.email)
        user.email = command.email
        if (command.unencryptedPassword.isPresent) {
            verifyPasswordIsNotWeak(command.unencryptedPassword.get())
            user.cryptedPassword = passwords.encrypt(command.unencryptedPassword.get())
        }
        user.firstName = command.firstName
        user.lastName = command.lastName
        users.update(user)
    }

    private fun verifyMissingUpdateInformation(command: UpdateProfileCommand) {
        if (command.email.email == null || command.email.email.isEmpty() ||
            (command.unencryptedPassword.isPresent && command.unencryptedPassword.get().password.isEmpty()) ||
            command.firstName.firstName == null || command.firstName.firstName.isEmpty()||
            command.lastName.lastName == null || command.lastName.lastName.isEmpty()
        )
            throw MissingInformationException()
    }

    private fun verifyNoMissingInformation(command: RegisterInternalUserCommand) {
        if (command.email.email == null || command.email.email.isEmpty() ||
            command.username.username == null || command.username.username.isEmpty() ||
            command.unencryptedPassword.password.isEmpty() ||
            command.firstName.firstName == null || command.firstName.firstName.isEmpty()||
            command.lastName.lastName == null || command.lastName.lastName.isEmpty()
        )
            throw MissingInformationException()
    }

    private fun verifyPasswordIsNotWeak(unencryptedPassword: UnencryptedPassword) {
        if (!PasswordUtility.isStrong(unencryptedPassword))
            throw WeakPasswordException()
    }

    private fun verifyEmailValidAndDoesNotExists(email: Email) {
        if (!EmailUtility.isValid(email))
            throw InvalidEmailException()

        if (users.exists(email))
            throw EmailAlreadyExistsException()
    }

    private fun verifyUsernameDoesNotExists(username: Username) {
        if (users.exists(username))
            throw UsernameAlreadyExistsException()
    }
}

class MissingInformationException : MunicipalityException()
class WeakPasswordException : MunicipalityException()
class UsernameAlreadyExistsException : MunicipalityException()
class EmailAlreadyExistsException : MunicipalityException()
class InvalidEmailException : MunicipalityException()
class IncorrectUsernameOrPasswordException : MunicipalityException()