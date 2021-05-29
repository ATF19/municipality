package com.municipality.backend.domain.model.user

import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class SystemUserTest {

    @Test(groups = [TestGroup.UNIT])
    fun is_only_system_user() {
        // given

        // when
        val user = SystemUser()

        // then
        assertThat(user.isAdmin()).isFalse
        assertThat(user.isSystem()).isTrue
        assertThat(user.isAnonymous()).isFalse
        assertThat(user.isResponsible(MunicipalityId())).isFalse
        assertThat(user.isResponsible(DistrictId())).isFalse
        assertThat(user.isAuditor(MunicipalityId())).isFalse
        assertThat(user.isAuditor(DistrictId())).isFalse
    }
}