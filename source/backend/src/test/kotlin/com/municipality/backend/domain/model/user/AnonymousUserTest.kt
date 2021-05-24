package com.municipality.backend.domain.model.user

import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.municipality.district.DistrictId
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions
import org.testng.annotations.Test

class AnonymousUserTest {

    @Test(groups = [TestGroup.UNIT])
    fun is_only_anonymous_user() {
        // given

        // when
        val user = AnonymousUser()

        // then
        Assertions.assertThat(user.isAdmin()).isFalse
        Assertions.assertThat(user.isSystem()).isFalse
        Assertions.assertThat(user.isAnonymous()).isTrue
        Assertions.assertThat(user.isResponsible(MunicipalityId())).isFalse
        Assertions.assertThat(user.isResponsible(DistrictId())).isFalse
        Assertions.assertThat(user.isAuditor(MunicipalityId())).isFalse
        Assertions.assertThat(user.isAuditor(DistrictId())).isFalse
    }
}