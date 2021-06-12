package com.municipality.backend.rest.complaint

import com.municipality.backend.application.complaint.ComplaintAppService
import com.municipality.backend.application.complaint.CreateComplaintCommand
import com.municipality.backend.application.complaint.UpdateComplaintCommand
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.complaint.*
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.FIRST_PAGE
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.file.ContentType
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.FirstName
import com.municipality.backend.domain.model.user.LastName
import com.municipality.backend.domain.service.file.Files
import com.municipality.backend.infrastructure.file.FileUtility
import com.municipality.backend.rest.core.PageDto
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/complaint")
@Transactional
class ComplaintRestService(
    private val complaintAppService: ComplaintAppService,
    private val files: Files,
    private val fileUtility: FileUtility,
    private val loggedInUserResolver: LoggedInUserResolver
) {

    @PostMapping
    fun createComplaint(@RequestBody request: CreateComplaintRequest): ResponseEntity<ComplaintDto> {
        val file = fileUtility.create(request.photo, ContentType("image/jpeg"))
        files.save(file)
        val command = CreateComplaintCommand(
            loggedInUserResolver.loggedIn(),
            Address(request.address),
            file,
            if (request.comment != null) Comment(request.comment) else null,
            request.position,
            PersonalInfo(
                FirstName(request.personalInfo?.firstName), LastName(request.personalInfo?.lastName),
                Phone(request.personalInfo?.phone), Email(request.personalInfo?.email)
            )
        )
        val created = complaintAppService.create(command)
        return ResponseEntity.ok(toDto(created))
    }

    @GetMapping("all")
    fun allComplaints(@RequestParam("page") page: Int?): ResponseEntity<PageDto<Complaint, ComplaintDto>> {
        val all = complaintAppService.all(
            loggedInUserResolver.loggedIn(),
            if (page == null) FIRST_PAGE else PageNumber(page), DEFAULT_PAGE_SIZE
        )
        return ResponseEntity.ok(PageDto(all) { toDto(it) })
    }

    @GetMapping("rejected")
    fun rejectedComplaints(@RequestParam("page") page: Int?): ResponseEntity<PageDto<Complaint, ComplaintDto>> {
        val all = complaintAppService.rejected(
            loggedInUserResolver.loggedIn(),
            if (page == null) FIRST_PAGE else PageNumber(page), DEFAULT_PAGE_SIZE
        )
        return ResponseEntity.ok(PageDto(all) { toDto(it) })
    }

    @GetMapping("{complaintId}")
    fun complaintById(@PathVariable("complaintId") id: String): ResponseEntity<ComplaintDto> =
        ResponseEntity.ok(toDto(complaintAppService.by(ComplaintId(id))))

    @GetMapping("code/{complaintCode}")
    fun complaintByCode(@PathVariable("complaintCode") code: String): ResponseEntity<ComplaintDto> =
        ResponseEntity.ok(toDto(complaintAppService.by(ComplaintCode(code))))

    @GetMapping("byIds")
    fun complaintsByIds(
        @RequestParam("complaintId") complaintIds: List<ComplaintId>,
        @RequestParam("page") page: Int?
    ): ResponseEntity<PageDto<Complaint, ComplaintDto>> {
        val complaints = complaintAppService.by(
            complaintIds,
            if (page == null) FIRST_PAGE else PageNumber(page), DEFAULT_PAGE_SIZE
        )
        return ResponseEntity.ok(PageDto(complaints) { toDto(it) })
    }

    @PutMapping("{complaintId}")
    fun updateComplaint(
        @PathVariable("complaintId") id: String,
        @RequestBody request: UpdateComplaintRequest
    ): ResponseEntity<String> {
        val command = UpdateComplaintCommand(
            loggedInUserResolver.loggedIn(),
            ComplaintId(id),
            Status.valueOf(request.status),
            if (request.resultComment != null) ResultComment(request.resultComment) else null
        )
        complaintAppService.update(command)
        return ResponseEntity.ok().build()
    }

    private fun toDto(complaint: Complaint): ComplaintDto {
        return ComplaintDto(
            complaint.id.rawId.toString(),
            complaint.code.code!!,
            complaint.comment?.comment,
            complaint.address.address!!,
            complaint.personalInfo,
            complaint.position,
            complaint.status.name,
            complaint.resultComment,
            fileUtility.asUrl(complaint.picture),
            complaint.district.id.rawId.toString()
        )
    }
}


data class ComplaintDto(
    val id: String, val code: String, val comment: String?, val address: String, val personalInfo: PersonalInfo?,
    val position: Position?, val status: String, val resultComment: ResultComment?, val pictureUrl: String,
    val districtId: String
)

data class PersonalInfoDto(val firstName: String?, val lastName: String?, val email: String?, val phone: String?)
data class CreateComplaintRequest(
    val photo: String,
    val address: String,
    val comment: String?,
    val position: Position?,
    val personalInfo: PersonalInfoDto?
)

data class UpdateComplaintRequest(val status: String, val resultComment: String?)