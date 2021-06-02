package com.municipality.backend.application.complaint

import com.municipality.backend.application.Command
import com.municipality.backend.domain.model.complaint.*
import com.municipality.backend.domain.model.file.File
import com.municipality.backend.domain.model.user.User

data class CreateComplaintCommand(
    override val user: User<*>,
    val address: Address,
    val picture: File,
    val comment: Comment?,
    var position: Position?,
    val personalInfo: PersonalInfo?
) : Command()

data class UpdateComplaintCommand(
    override val user: User<*>,
    val commandId: ComplaintId,
    val status: Status?,
    val resultComment: ResultComment?
) : Command()