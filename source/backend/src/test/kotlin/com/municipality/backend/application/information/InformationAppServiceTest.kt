package com.municipality.backend.application.information

import com.municipality.backend.application.user.InvalidEmailException
import com.municipality.backend.domain.model.complaint.Phone
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.information.Information
import com.municipality.backend.domain.model.information.InformationBuilder
import com.municipality.backend.domain.model.information.Intro
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.RegisteredUserBuilder
import com.municipality.backend.domain.service.information.Informations
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.testng.annotations.Test

class InformationAppServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_when_non_admin_tries_to_save_info() {
        // given
        val user = RegisteredUserBuilder().build()
        val informations = mockk<Informations>()
        val informationAppService = InformationAppService(informations)
        val command = SaveInformationCommand(user, Intro("new Intro"), Phone("12345678"), Email("test@demo.com"))

        // when

        // then
        assertThatThrownBy { informationAppService.save(command) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_when_provided_with_invalid_email() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val informations = mockk<Informations>(relaxed = true)
        val informationAppService = InformationAppService(informations)
        val command = SaveInformationCommand(user, Intro("new Intro"), Phone("12345678"), Email("d.com"))

        // when

        // then
        assertThatThrownBy { informationAppService.save(command) }
            .isInstanceOf(InvalidEmailException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun save_info() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val informations = mockk<Informations>(relaxed = true)
        val informationAppService = InformationAppService(informations)
        every { informations.get() }.returns(InformationBuilder().build())
        val command = SaveInformationCommand(user, Intro("new Intro"), Phone("12345678"), Email("test@demo.com"))

        // when
        informationAppService.save(command)

        // then
        val expectedInfo = Information()
        expectedInfo.intro = command.intro
        expectedInfo.email = command.email
        expectedInfo.phone = command.phone
        verify { informations.save(expectedInfo) }
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_info() {
        // given
        val informations = mockk<Informations>()
        val informationAppService = InformationAppService(informations)
        val information = InformationBuilder().build()
        every { informations.get() }.returns(information)

        // when
        val result = informationAppService.get()

        // then
        assertThat(result).isEqualTo(information)
    }
}