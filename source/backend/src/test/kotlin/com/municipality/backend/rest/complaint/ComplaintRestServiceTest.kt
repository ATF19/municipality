package com.municipality.backend.rest.complaint

import com.municipality.backend.application.complaint.ComplaintAppService
import com.municipality.backend.application.complaint.CreateComplaintCommand
import com.municipality.backend.application.complaint.UpdateComplaintCommand
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.complaint.*
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.file.ContentType
import com.municipality.backend.domain.model.file.File
import com.municipality.backend.domain.model.file.FileName
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.FirstName
import com.municipality.backend.domain.model.user.LastName
import com.municipality.backend.domain.service.file.Files
import com.municipality.backend.infrastructure.file.FileUtility
import com.municipality.backend.shared_code_for_tests.LoggedInUserForTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.testng.annotations.Test

class ComplaintRestServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun create_complaint() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ComplaintAppService>()
        val files = mockk<Files>(relaxed = true)
        val fileUtility = mockk<FileUtility>()
        val restService = ComplaintRestService(appService, files, fileUtility, loggedInUserResolver)
        val fileName = "Name"
        val content = "DummyContent"
        val request = CreateComplaintRequest("photoBase64", "demo address", "comment", null, null)
        val url = "dummyUrl"
        val dummyFile = File()
        dummyFile.fileName = FileName(fileName)
        val contentType = ContentType("image/jpeg")
        dummyFile.contentType = contentType
        dummyFile.blob = content.encodeToByteArray()
        every { fileUtility.create(request.photo, contentType) }.returns(dummyFile)
        val expectedCommand = CreateComplaintCommand(loggedInUserResolver.loggedIn(), Address(request.address), dummyFile,
            Comment(request.comment), null, PersonalInfo(FirstName(), LastName(), Phone(), Email())
        )
        val complaint = ComplaintBuilder().build()
        complaint.picture = dummyFile
        every { appService.create(expectedCommand) }.returns(complaint)
        every { fileUtility.asUrl(complaint.picture) }.returns(url)

        // when
        val response = restService.createComplaint(request)

        // then
        verify { fileUtility.create(request.photo, contentType) }
        verify { files.save(dummyFile) }
        verify { appService.create(expectedCommand) }
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.id).isEqualTo(complaint.id.rawId.toString())
        assertThat(response.body!!.address).isEqualTo(complaint.address.address)
        assertThat(response.body!!.comment).isEqualTo(complaint.comment!!.comment)
        assertThat(response.body!!.districtId).isEqualTo(complaint.district.id.rawId.toString())
        assertThat(response.body!!.pictureUrl).isEqualTo(url)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_all_complaints() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ComplaintAppService>()
        val files = mockk<Files>()
        val fileUtility = mockk<FileUtility>(relaxed = true)
        val restService = ComplaintRestService(appService, files, fileUtility, loggedInUserResolver)
        val pageNumber = PageNumber(2)
        val complaint1 = ComplaintBuilder().build()
        val complaint2 = ComplaintBuilder().build()
        val complaint3 = ComplaintBuilder().build()
        val page = Page(listOf(complaint1, complaint2, complaint3), pageNumber, DEFAULT_PAGE_SIZE, 1)
        every { appService.all(loggedInUserResolver.loggedIn(), pageNumber, DEFAULT_PAGE_SIZE) }.returns(page)

        // when
        val response = restService.allComplaints(pageNumber.number)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.elements).hasSize(3)
        assertThat(response.body!!.elements.map { it.id })
            .containsExactlyInAnyOrder(complaint1.id.rawId.toString(), complaint2.id.rawId.toString(), complaint3.id.rawId.toString())
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_rejected_complaints() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ComplaintAppService>()
        val files = mockk<Files>()
        val fileUtility = mockk<FileUtility>(relaxed = true)
        val restService = ComplaintRestService(appService, files, fileUtility, loggedInUserResolver)
        val pageNumber = PageNumber(2)
        val complaint1 = ComplaintBuilder().build()
        val complaint2 = ComplaintBuilder().build()
        val complaint3 = ComplaintBuilder().build()
        val page = Page(listOf(complaint1, complaint2, complaint3), pageNumber, DEFAULT_PAGE_SIZE, 1)
        every { appService.rejected(loggedInUserResolver.loggedIn(), pageNumber, DEFAULT_PAGE_SIZE) }.returns(page)

        // when
        val response = restService.rejectedComplaints(pageNumber.number)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.elements).hasSize(3)
        assertThat(response.body!!.elements.map { it.id })
            .containsExactlyInAnyOrder(complaint1.id.rawId.toString(), complaint2.id.rawId.toString(), complaint3.id.rawId.toString())
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_complaint_by_id() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ComplaintAppService>()
        val files = mockk<Files>()
        val fileUtility = mockk<FileUtility>(relaxed = true)
        val restService = ComplaintRestService(appService, files, fileUtility, loggedInUserResolver)
        val complaint = ComplaintBuilder().build()
        every { appService.by(complaint.id) }.returns(complaint)

        // when
        val response = restService.complaintById(complaint.id.rawId.toString())

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.id).isEqualTo(complaint.id.rawId.toString())
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_complaint_by_code() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ComplaintAppService>()
        val files = mockk<Files>()
        val fileUtility = mockk<FileUtility>(relaxed = true)
        val restService = ComplaintRestService(appService, files, fileUtility, loggedInUserResolver)
        val complaint = ComplaintBuilder().build()
        every { appService.by(complaint.code) }.returns(complaint)

        // when
        val response = restService.complaintByCode(complaint.code.code!!)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.id).isEqualTo(complaint.id.rawId.toString())
    }

    @Test(groups = [TestGroup.UNIT])
    fun update_complaint() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ComplaintAppService>(relaxed = true)
        val files = mockk<Files>(relaxed = true)
        val fileUtility = mockk<FileUtility>()
        val restService = ComplaintRestService(appService, files, fileUtility, loggedInUserResolver)
        val complaint = ComplaintBuilder().build()
        val expectedCommand = UpdateComplaintCommand(loggedInUserResolver.loggedIn(), complaint.id, Status.IN_PROGRESS,
            ResultComment("WIP"))
        val request = UpdateComplaintRequest(Status.IN_PROGRESS.name, "WIP")

        // when
        val response = restService.updateComplaint(complaint.id.rawId.toString(), request)

        // then
        verify { appService.update(expectedCommand) }
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_all_complaints_by_ids() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ComplaintAppService>()
        val files = mockk<Files>()
        val fileUtility = mockk<FileUtility>(relaxed = true)
        val restService = ComplaintRestService(appService, files, fileUtility, loggedInUserResolver)
        val pageNumber = PageNumber(2)
        val complaint1 = ComplaintBuilder().build()
        val complaint2 = ComplaintBuilder().build()
        val complaint3 = ComplaintBuilder().build()
        val page = Page(listOf(complaint1, complaint2, complaint3), pageNumber, DEFAULT_PAGE_SIZE, 1)
        every { appService.by(listOf(complaint1.id, complaint2.id, complaint3.id), pageNumber, DEFAULT_PAGE_SIZE) }.returns(page)

        // when
        val response = restService.complaintsByIds(listOf(complaint1.id, complaint2.id, complaint3.id), pageNumber.number)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.elements).hasSize(3)
        assertThat(response.body!!.elements.map { it.id })
            .containsExactlyInAnyOrder(complaint1.id.rawId.toString(), complaint2.id.rawId.toString(), complaint3.id.rawId.toString())
    }
}