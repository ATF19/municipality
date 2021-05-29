package com.municipality.backend.application.municipality

import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.municipality.MunicipalityBuilder
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.user.RegisteredUserBuilder
import com.municipality.backend.domain.model.user.role.MunicipalityResponsible
import com.municipality.backend.domain.service.municipality.Municipalities
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.testng.annotations.Test

class MunicipalityAppServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_not_admin_and_not_municipality_role() {
        // given
        val user = RegisteredUserBuilder.DEFAULT
        val municipalities = mockk<Municipalities>()
        val appService = MunicipalityAppService(municipalities)

        // when

        // then
        assertThatThrownBy { appService.all(user, PageNumber(1), DEFAULT_PAGE_SIZE) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_all_if_user_is_admin() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val municipalities = mockk<Municipalities>()
        val appService = MunicipalityAppService(municipalities)
        val municipality1 = MunicipalityBuilder().build()
        val municipality2 = MunicipalityBuilder().build()
        val municipality3 = MunicipalityBuilder().build()
        val pageNumber = PageNumber(1)
        val pageSize = DEFAULT_PAGE_SIZE

        val page = Page(listOf(municipality1, municipality2, municipality3), pageNumber, pageSize, 1)
        every { municipalities.all(pageNumber, pageSize) }.returns(page)

        // when
        val result = appService.all(user, pageNumber, pageSize)

        // then
        assertThat(result).isEqualTo(page)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_users_municipalities() {
        // given
        val user = RegisteredUserBuilder().build()
        val municipalityId = MunicipalityId()
        user.roles.grant(MunicipalityResponsible(municipalityId))
        val municipalities = mockk<Municipalities>()
        val appService = MunicipalityAppService(municipalities)
        val municipality1 = MunicipalityBuilder().build()
        val municipality2 = MunicipalityBuilder().build()
        val municipality3 = MunicipalityBuilder().build()
        val pageNumber = PageNumber(1)
        val pageSize = DEFAULT_PAGE_SIZE

        val page = Page(listOf(municipality1, municipality2, municipality3), pageNumber, pageSize, 1)
        every { municipalities.by(setOf(municipalityId), pageNumber, pageSize) }.returns(page)

        // when
        val result = appService.all(user, pageNumber, pageSize)

        // then
        assertThat(result).isEqualTo(page)
    }
}