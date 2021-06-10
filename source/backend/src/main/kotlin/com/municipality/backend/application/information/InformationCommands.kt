package com.municipality.backend.application.information

import com.municipality.backend.application.Command
import com.municipality.backend.domain.model.complaint.Phone
import com.municipality.backend.domain.model.information.Intro
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.User

data class SaveInformationCommand(
    override val user: User<*>,
    val intro: Intro,
    val phone: Phone,
    val email: Email
) : Command()