package com.municipality.backend.application.district

import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.district.DistrictBuilder
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.user.RegisteredUserBuilder
import com.municipality.backend.domain.model.user.role.DistrictResponsible
import com.municipality.backend.domain.service.district.Districts
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.testng.annotations.Test

class DistrictAppServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_if_not_admin_and_no_municipality_role_and_no_district_role() {
        // given
        val user = RegisteredUserBuilder.DEFAULT
        val districts = mockk<Districts>()
        val appService = DistrictAppService(districts)

        // when

        // then
        Assertions.assertThatThrownBy { appService.all(user, PageNumber(1), DEFAULT_PAGE_SIZE) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_all_if_user_is_admin() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val districts = mockk<Districts>()
        val appService = DistrictAppService(districts)
        val district1 = DistrictBuilder().build()
        val district2 = DistrictBuilder().build()
        val district3 = DistrictBuilder().build()
        val pageNumber = PageNumber(1)
        val pageSize = DEFAULT_PAGE_SIZE

        val page = Page(listOf(district1, district2, district3), pageNumber, pageSize, 1)
        every { districts.all(pageNumber, pageSize) }.returns(page)

        // when
        val result = appService.all(user, pageNumber, pageSize)

        // then
        Assertions.assertThat(result).isEqualTo(page)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_users_districts() {
        // given
        val user = RegisteredUserBuilder().build()
        val districtId = DistrictId()
        user.roles.grant(DistrictResponsible(districtId))
        val districts = mockk<Districts>()
        val appService = DistrictAppService(districts)
        val district1 = DistrictBuilder().build()
        val district2 = DistrictBuilder().build()
        val district3 = DistrictBuilder().build()
        val pageNumber = PageNumber(1)
        val pageSize = DEFAULT_PAGE_SIZE

        val page = Page(listOf(district1, district2, district3), pageNumber, pageSize, 1)
        every { districts.by(setOf(districtId), emptySet(), pageNumber, pageSize) }.returns(page)

        // when
        val result = appService.all(user, pageNumber, pageSize)

        // then
        Assertions.assertThat(result).isEqualTo(page)
    }
}