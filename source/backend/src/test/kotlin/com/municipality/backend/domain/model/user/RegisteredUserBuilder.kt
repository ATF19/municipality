package com.municipality.backend.domain.model.user

import com.municipality.backend.domain.model.user.role.Roles
import java.util.*


class RegisteredUserBuilder {
    var id = RegisteredUserId()
    var username = Username(UUID.randomUUID().toString())
    var email = Email("${UUID.randomUUID()}@test.com")
    var cryptedPassword = CryptedPassword("StrongPass@Demo_net")
    var firstName = FirstName("John")
    var lastName = LastName("Doe")
    var roles = Roles.empty()

    fun build(): RegisteredUser {
        val registeredUser = RegisteredUser(id)
        registeredUser.username = username
        registeredUser.email = email
        registeredUser.cryptedPassword = cryptedPassword
        registeredUser.firstName = firstName
        registeredUser.lastName = lastName
        registeredUser.roles = roles
        return registeredUser
    }
}