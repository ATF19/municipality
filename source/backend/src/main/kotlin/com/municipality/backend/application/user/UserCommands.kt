package com.municipality.backend.application.user

import com.municipality.backend.application.Command
import com.municipality.backend.domain.model.user.*
import java.util.*

data class RegisterInternalUserCommand(
    override val user: User<*>,
    val username: Username,
    val email: Email,
    val unencryptedPassword: UnencryptedPassword,
    val firstName: FirstName,
    val lastName: LastName,
    val isAdmin: Boolean
) : Command()

data class UpdateProfileCommand(
    override val user: User<*>,
    val email: Email,
    val unencryptedPassword: Optional<UnencryptedPassword>,
    val firstName: FirstName,
    val lastName: LastName
) : Command()

data class UpdateInternalUserCommand(
    override val user: User<*>,
    val email: Email,
    val unencryptedPassword: Optional<UnencryptedPassword>,
    val firstName: FirstName,
    val lastName: LastName,
    val isAdmin: Boolean
) : Command()

data class DeleteUserCommand(
    override val user: User<*>,
    val userId: RegisteredUserId
) : Command()