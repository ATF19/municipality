package com.municipality.backend.rest.district

import com.municipality.backend.application.district.DistrictAppService
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.district.DistrictBuilder
import com.municipality.backend.rest.core.PageDto
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.springframework.http.HttpStatus
import org.testng.annotations.Test

class DistrictRestServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun get_all() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        val appService = mockk<DistrictAppService>()
        val restService = DistrictRestService(appService, loggedInUserResolver)
        val district1 = DistrictBuilder().build()
        val district2 = DistrictBuilder().build()
        val page = Page(listOf(district1, district2), PageNumber(1), DEFAULT_PAGE_SIZE, 1)
        every { appService.all(loggedInUserResolver.loggedIn(), PageNumber(1), DEFAULT_PAGE_SIZE) }.returns(page)

        // when
        val response = restService.all(1)

        // then
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(PageDto(page) {
            DistrictDto(
                it.id.rawId.toString(),
                it.name.name!!,
                it.nameInArabic.nameInArabic!!,
                it.municipality.id.rawId.toString(),
                it.municipality.name.name!!,
                it.municipality.nameInArabic.nameInArabic!!
            )
        })
    }
}