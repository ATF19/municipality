package com.municipality.backend.shared_code_for_tests

import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.municipality.district.DistrictId
import com.municipality.backend.domain.model.user.User
import com.municipality.backend.domain.model.user.UserId
import java.util.*

class TestUser : User<TestUserId> {
    override fun id() = TestUserId()

    override fun name() = "TEST"

    override fun isAdmin() = false

    override fun isRegistered() = false

    override fun isSystem() = false

    override fun isAnonymous() = false

    override fun isResponsible(municipalityId: MunicipalityId) = false

    override fun isResponsible(districtId: DistrictId) = false

    override fun isAuditor(municipalityId: MunicipalityId) = false

    override fun isAuditor(districtId: DistrictId) = false
}

class TestUserId : UserId(UUID.nameUUIDFromBytes("TEST".toByteArray()))