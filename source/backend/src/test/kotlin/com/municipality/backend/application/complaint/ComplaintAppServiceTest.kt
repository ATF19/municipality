package com.municipality.backend.application.complaint

import com.municipality.backend.application.user.MissingInformationException
import com.municipality.backend.domain.model.complaint.*
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.district.DistrictBuilder
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.file.ContentType
import com.municipality.backend.domain.model.file.File
import com.municipality.backend.domain.model.file.FileName
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.user.RegisteredUserBuilder
import com.municipality.backend.domain.model.user.role.DistrictAuditor
import com.municipality.backend.domain.model.user.role.MunicipalityResponsible
import com.municipality.backend.domain.service.complaint.Complaints
import com.municipality.backend.domain.service.district.Districts
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.testng.annotations.Test

class ComplaintAppServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_provided_with_missing_info() {
        // given
        val user = RegisteredUserBuilder.DEFAULT
        val complaints = mockk<Complaints>()
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val command = CreateComplaintCommand(user, Address(null), File(), null, null, null)

        // when

        // then
        assertThatThrownBy { appService.create(command) }.isInstanceOf(MissingInformationException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun create_complaint() {
        // given
        val user = RegisteredUserBuilder.DEFAULT
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val district = DistrictBuilder().build()
        val code = ComplaintCode("TestCode")
        val picture = File()
        picture.fileName = FileName("test")
        picture.contentType = ContentType("demo")
        picture.blob = "demo".encodeToByteArray()
        val command = CreateComplaintCommand(user, Address("address"), picture, null, Position(20.4, 12.6), null)
        every { districts.by(DistrictId("f9cebd21-2cb4-4849-aa19-5a197bb59ad5")) }.returns(district)
        every { complaintCodeGenerator.generate() }.returns(code)

        // when
        val created = appService.create(command)

        // then
        verify { complaints.create(created) }
        assertThat(created.code).isEqualTo(code)
        assertThat(created.status).isEqualTo(Status.CREATED)
        assertThat(created.address).isEqualTo(command.address)
        assertThat(created.picture).isEqualTo(command.picture)
        assertThat(created.comment).isEqualTo(command.comment)
        assertThat(created.position).isEqualTo(command.position)
        assertThat(created.personalInfo).isEqualTo(command.personalInfo)
        assertThat(created.district).isEqualTo(district)
        assertThat(created.resultComment).isNull()
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_non_authorized_user_calls_all() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val pageSize = DEFAULT_PAGE_SIZE
        val pageNumber = PageNumber(1)

        // when

        // then
        assertThatThrownBy { appService.all(user, pageNumber, pageSize) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_all() {
        // given
        val builder = RegisteredUserBuilder()
        val municipalityId = MunicipalityId()
        val districtId = DistrictId()
        builder.roles.grant(MunicipalityResponsible(municipalityId))
        builder.roles.grant(DistrictAuditor(districtId))
        val user = builder.build()
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val pageSize = DEFAULT_PAGE_SIZE
        val pageNumber = PageNumber(1)
        val complaint = ComplaintBuilder().build()
        val page = Page(listOf(complaint), pageNumber, pageSize, 1)
        every { complaints.all(user.roles.municipalities(), user.roles.districts(), pageNumber, pageSize) }.returns(page)

        // when
        val all = appService.all(user, pageNumber, pageSize)

        // then
        verify { complaints.all(user.roles.municipalities(), user.roles.districts(), pageNumber, pageSize) }
        assertThat(all.elements).containsOnly(complaint)
        assertThat(all.pageNumber).isEqualTo(pageNumber)
        assertThat(all.pageSize).isEqualTo(pageSize)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_non_authorized_user_calls_rejected() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val pageSize = DEFAULT_PAGE_SIZE
        val pageNumber = PageNumber(1)

        // when

        // then
        assertThatThrownBy { appService.rejected(user, pageNumber, pageSize) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_rejected() {
        // given
        val builder = RegisteredUserBuilder()
        val municipalityId = MunicipalityId()
        val districtId = DistrictId()
        builder.roles.grant(MunicipalityResponsible(municipalityId))
        builder.roles.grant(DistrictAuditor(districtId))
        val user = builder.build()
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val pageSize = DEFAULT_PAGE_SIZE
        val pageNumber = PageNumber(1)
        val complaint = ComplaintBuilder().build()
        val page = Page(listOf(complaint), pageNumber, pageSize, 1)
        every { complaints.rejected(user.roles.municipalities(), user.roles.districts(), pageNumber, pageSize) }.returns(page)

        // when
        val all = appService.rejected(user, pageNumber, pageSize)

        // then
        verify { complaints.rejected(user.roles.municipalities(), user.roles.districts(), pageNumber, pageSize) }
        assertThat(all.elements).containsOnly(complaint)
        assertThat(all.pageNumber).isEqualTo(pageNumber)
        assertThat(all.pageSize).isEqualTo(pageSize)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_by_id() {
        // given
        val user = RegisteredUserBuilder.DEFAULT
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val complaint = ComplaintBuilder().build()
        every { complaints.by(complaint.id) }.returns(complaint)

        // when
        val result = appService.by(complaint.id)

        // then
        verify { complaints.by(complaint.id) }
        assertThat(result).isEqualTo(complaint)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_by_code() {
        // given
        val user = RegisteredUserBuilder.DEFAULT
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val complaint = ComplaintBuilder().build()
        every { complaints.by(complaint.code) }.returns(complaint)

        // when
        val result = appService.by(complaint.code)

        // then
        verify { complaints.by(complaint.code) }
        assertThat(result).isEqualTo(complaint)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_non_authorized_user_calls_update() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val complaint = ComplaintBuilder().build()
        val command = UpdateComplaintCommand(user, complaint.id, Status.IN_PROGRESS, ResultComment("Demo"))

        // when

        // then
        assertThatThrownBy { appService.update(command) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_missing_information_when_updating() {
        // given
        val user = RegisteredUserBuilder().municipalityResponsible().build()
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val complaint = ComplaintBuilder().build()
        val command = UpdateComplaintCommand(user, complaint.id, null, null)

        // when

        // then
        assertThatThrownBy { appService.update(command) }
            .isInstanceOf(MissingInformationException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun update() {
        // given
        val user = RegisteredUserBuilder().municipalityResponsible().build()
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val complaint = ComplaintBuilder().build()
        val resultComment = ResultComment("Demo")
        val command = UpdateComplaintCommand(user, complaint.id, Status.IN_PROGRESS, resultComment)
        every { complaints.by(complaint.id) }.returns(complaint)

        // when
        appService.update(command)

        // then
        val captor = slot<Complaint>()
        verify { complaints.update(capture(captor)) }
        val result = captor.captured
        assertThat(result.status).isEqualTo(Status.IN_PROGRESS)
        assertThat(result.resultComment).isEqualTo(resultComment)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_all_by_ids() {
        // given
        val complaints = mockk<Complaints>(relaxed = true)
        val districts = mockk<Districts>()
        val complaintCodeGenerator = mockk<ComplaintCodeGenerator>()
        val appService = ComplaintAppService(complaints, districts, complaintCodeGenerator)
        val pageSize = DEFAULT_PAGE_SIZE
        val pageNumber = PageNumber(1)
        val complaint1 = ComplaintBuilder().build()
        val complaint2 = ComplaintBuilder().build()
        val page = Page(listOf(complaint1, complaint2), pageNumber, pageSize, 1)
        every { complaints.by(listOf(complaint1.id, complaint2.id), pageNumber, pageSize) }.returns(page)

        // when
        val all = appService.by(listOf(complaint1.id, complaint2.id), pageNumber, pageSize)

        // then
        assertThat(all.elements).containsExactlyInAnyOrder(complaint1, complaint2)
        assertThat(all.pageNumber).isEqualTo(pageNumber)
        assertThat(all.pageSize).isEqualTo(pageSize)
    }
}