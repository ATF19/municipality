package com.municipality.backend.rest.information

import com.municipality.backend.application.information.InformationAppService
import com.municipality.backend.application.information.SaveInformationCommand
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.complaint.Phone
import com.municipality.backend.domain.model.information.InformationBuilder
import com.municipality.backend.domain.model.information.Intro
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.shared_code_for_tests.LoggedInUserForTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.testng.annotations.Test

class InformationRestServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun get_information() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<InformationAppService>()
        val restService = InformationRestService(appService, loggedInUserResolver)
        val information = InformationBuilder().build()
        every { appService.get() }.returns(information)

        // when
        val result = restService.getInformation()

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body!!.intro).isEqualTo(information.intro.intro)
        assertThat(result.body!!.email).isEqualTo(information.email.email)
        assertThat(result.body!!.phone).isEqualTo(information.phone.phoneNumber)
    }

    @Test(groups = [TestGroup.UNIT])
    fun save_information() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<InformationAppService>(relaxed = true)
        val restService = InformationRestService(appService, loggedInUserResolver)
        val informationDto = InformationDto("TestIntro", "", "atef@demo.com")

        // when
        val result = restService.saveInformation(informationDto)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        val expectedCommand = SaveInformationCommand(
            loggedInUserResolver.loggedIn(),
            Intro(informationDto.intro), Phone(informationDto.phone), Email(informationDto.email)
        )
        verify { appService.save(expectedCommand) }
    }
}