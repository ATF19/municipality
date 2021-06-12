package com.municipality.backend.application.complaint

import com.municipality.backend.application.user.MissingInformationException
import com.municipality.backend.domain.model.complaint.Complaint
import com.municipality.backend.domain.model.complaint.ComplaintCode
import com.municipality.backend.domain.model.complaint.ComplaintId
import com.municipality.backend.domain.model.complaint.Status
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.User
import com.municipality.backend.domain.service.complaint.Complaints
import com.municipality.backend.domain.service.district.Districts
import org.springframework.stereotype.Component

@Component
class ComplaintAppService(
    private val complaints: Complaints,
    private val districts: Districts,
    private val complaintCodeGenerator: ComplaintCodeGenerator
) {

    private val defaultDistrictId = DistrictId("f9cebd21-2cb4-4849-aa19-5a197bb59ad5")

    fun create(command: CreateComplaintCommand): Complaint {
        verifyNoMissingInformation(command)
        val generatedCode = complaintCodeGenerator.generate()
        val district = districts.by(defaultDistrictId)
        val complaint = Complaint()
        complaint.code = generatedCode
        complaint.address = command.address
        complaint.comment = command.comment
        complaint.picture = command.picture
        complaint.position = command.position
        complaint.personalInfo = command.personalInfo
        complaint.district = district
        complaint.status = Status.CREATED
        complaints.create(complaint)
        return complaint
    }

    fun all(user: User<*>, pageNumber: PageNumber, pageSize: PageSize): Page<Complaint> {
        if (!user.isMunicipalityResponsible() && !user.isMunicipalityAuditor() && !user.isDistrictResponsible() && !user.isDistrictAuditor())
            throw InsufficientPermissionException()

        if (user !is RegisteredUser)
            throw InsufficientPermissionException()

        return complaints.all(user.roles.municipalities(), user.roles.districts(), pageNumber, pageSize)
    }

    fun rejected(user: User<*>, pageNumber: PageNumber, pageSize: PageSize): Page<Complaint> {
        if (!user.isMunicipalityResponsible() && !user.isMunicipalityAuditor() && !user.isDistrictResponsible() && !user.isDistrictAuditor())
            throw InsufficientPermissionException()

        if (user !is RegisteredUser)
            throw InsufficientPermissionException()

        return complaints.rejected(user.roles.municipalities(), user.roles.districts(), pageNumber, pageSize)
    }

    fun by(complaintId: ComplaintId): Complaint = complaints.by(complaintId)

    fun by(complaintCode: ComplaintCode): Complaint = complaints.by(complaintCode)

    fun by(complaintIds: List<ComplaintId>, pageNumber: PageNumber, pageSize: PageSize): Page<Complaint> = complaints
        .by(complaintIds, pageNumber, pageSize)

    fun update(command: UpdateComplaintCommand) {
        if (!command.user.isMunicipalityResponsible() && !command.user.isMunicipalityAuditor() && !command.user.isDistrictResponsible() && !command.user.isDistrictAuditor())
            throw InsufficientPermissionException()

        if (command.user !is RegisteredUser)
            throw InsufficientPermissionException()

        verifyNoMissingInformation(command)
        val byId = complaints.by(command.commandId)
        if (command.status != null)
            byId.status = command.status
        if (command.resultComment != null)
            byId.resultComment = command.resultComment
        complaints.update(byId)
    }

    private fun verifyNoMissingInformation(command: CreateComplaintCommand) {
        if (command.address.address == null || command.address.address.isEmpty())
            throw MissingInformationException()
    }

    private fun verifyNoMissingInformation(command: UpdateComplaintCommand) {
        if (command.status == null)
            throw MissingInformationException()
    }
}