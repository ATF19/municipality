package com.municipality.backend.rest.municipality

import com.municipality.backend.application.municipality.MunicipalityAppService
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.municipality.MunicipalityBuilder
import com.municipality.backend.rest.core.PageDto
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.testng.annotations.Test

class MunicipalityRestServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun get_all() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        val municipalityAppService = mockk<MunicipalityAppService>()
        val restService = MunicipalityRestService(municipalityAppService, loggedInUserResolver)
        val municipality1 = MunicipalityBuilder().build()
        val municipality2 = MunicipalityBuilder().build()
        val page = Page(listOf(municipality1, municipality2), PageNumber(1), DEFAULT_PAGE_SIZE, 1)
        every { municipalityAppService.all(loggedInUserResolver.loggedIn(), PageNumber(1), DEFAULT_PAGE_SIZE) }.returns(page)

        // when
        val response = restService.all(1)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(PageDto(page) {
            MunicipalityDto(
                it.id.rawId.toString(),
                it.name.name!!,
                it.nameInArabic.nameInArabic!!
            )
        })
    }
}