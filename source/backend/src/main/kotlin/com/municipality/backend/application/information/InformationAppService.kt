package com.municipality.backend.application.information

import com.municipality.backend.application.user.InvalidEmailException
import com.municipality.backend.application.utility.EmailUtility
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.information.Information
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.service.information.Informations
import org.springframework.stereotype.Component

@Component
class InformationAppService(
    private val informations: Informations
) {

    fun save(command: SaveInformationCommand) {
        if (!command.user.isAdmin())
            throw InsufficientPermissionException()

        verifyEmailValid(command.email)
        val information = informations.get()
        information.intro = command.intro
        information.email = command.email
        information.phone = command.phone
        informations.save(information)
    }

    fun get(): Information = informations.get()

    private fun verifyEmailValid(email: Email) {
        if (!email.email.isNullOrEmpty() && !EmailUtility.isValid(email))
            throw InvalidEmailException()
    }
}