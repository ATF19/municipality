package com.municipality.backend.application.user

import com.municipality.backend.application.utility.EmailUtility
import com.municipality.backend.application.utility.PasswordUtility
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.core.error.MunicipalityException
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.User
import com.municipality.backend.domain.model.user.Username
import com.municipality.backend.domain.model.user.role.Admin
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
        if (!command.user.isRegistered() || command.user !is RegisteredUser)
            throw InsufficientPermissionException()

        verifyMissingUpdateInformation(command)
        if (command.user.email != command.email)
            verifyEmailValidAndDoesNotExists(command.email)
        command.user.email = command.email
        if (command.unencryptedPassword.isPresent) {
            verifyPasswordIsNotWeak(command.unencryptedPassword.get())
            command.user.cryptedPassword = passwords.encrypt(command.unencryptedPassword.get())
        }
        command.user.firstName = command.firstName
        command.user.lastName = command.lastName
        users.update(command.user)
    }

    fun all(user: User<*>, pageNumber: PageNumber, pageSize: PageSize): Page<RegisteredUser> {
        if (!user.isAdmin())
            throw InsufficientPermissionException()

        return users.all(pageNumber, pageSize)
    }

    fun updateInternalUser(command: UpdateInternalUserCommand) {
        if (!command.user.isAdmin() || command.user !is RegisteredUser)
            throw InsufficientPermissionException()

        verifyMissingUpdateInformation(command)
        if (command.user.email != command.email)
            verifyEmailValidAndDoesNotExists(command.email)
        command.user.email = command.email
        if (command.unencryptedPassword.isPresent) {
            verifyPasswordIsNotWeak(command.unencryptedPassword.get())
            command.user.cryptedPassword = passwords.encrypt(command.unencryptedPassword.get())
        }
        command.user.firstName = command.firstName
        command.user.lastName = command.lastName
        if (command.isAdmin) command.user.roles.grant(Admin()) else command.user.roles.revoke(Admin())
        users.update(command.user)
    }

    fun deleteUser(command: DeleteUserCommand) {
        if (!command.user.isAdmin())
            throw InsufficientPermissionException()

        val user = users.by(command.userId)
        users.delete(user)
    }

    private fun verifyMissingUpdateInformation(command: UpdateProfileCommand) {
        if (command.email.email == null || command.email.email.isEmpty() ||
            (command.unencryptedPassword.isPresent && command.unencryptedPassword.get().password.isEmpty()) ||
            command.firstName.firstName == null || command.firstName.firstName.isEmpty() ||
            command.lastName.lastName == null || command.lastName.lastName.isEmpty()
        )
            throw MissingInformationException()
    }

    private fun verifyMissingUpdateInformation(command: UpdateInternalUserCommand) {
        if (command.email.email == null || command.email.email.isEmpty() ||
            (command.unencryptedPassword.isPresent && command.unencryptedPassword.get().password.isEmpty()) ||
            command.firstName.firstName == null || command.firstName.firstName.isEmpty() ||
            command.lastName.lastName == null || command.lastName.lastName.isEmpty()
        )
            throw MissingInformationException()
    }

    private fun verifyNoMissingInformation(command: RegisterInternalUserCommand) {
        if (command.email.email == null || command.email.email.isEmpty() ||
            command.username.username == null || command.username.username.isEmpty() ||
            command.unencryptedPassword.password.isEmpty() ||
            command.firstName.firstName == null || command.firstName.firstName.isEmpty() ||
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