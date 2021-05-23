package com.municipality.backend.domain.model.user

import com.municipality.backend.domain.model.core.DomainEntity
import com.municipality.backend.domain.model.user.role.Roles
import javax.persistence.Embeddable
import javax.persistence.Entity

@Entity
class RegisteredUser : DomainEntity<RegisteredUserId>, User<RegisteredUserId> {
    lateinit var username: Username
    lateinit var email: Email
    lateinit var cryptedPassword: CryptedPassword
    lateinit var firstName: FirstName
    lateinit var lastName: LastName
    lateinit var roles: Roles

    constructor(): super(RegisteredUserId())

    constructor(id: RegisteredUserId): super(id)

    override fun id() = id
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as RegisteredUser

        if (username != other.username) return false
        if (email != other.email) return false
        if (cryptedPassword != other.cryptedPassword) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (roles != other.roles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + cryptedPassword.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + roles.hashCode()
        return result
    }

    override fun toString(): String {
        return "RegisteredUser(username=$username, email=$email, cryptedPassword=$cryptedPassword, firstName=$firstName, lastName=$lastName, roles=$roles)"
    }
}

@Embeddable
data class Username(val username: String? = null)

@Embeddable
data class Email(val email: String? = null)

@Embeddable
data class CryptedPassword(val password: String? = null)

@Embeddable
data class FirstName(val firstName: String? = null)

@Embeddable
data class LastName(val lastName: String? = null)