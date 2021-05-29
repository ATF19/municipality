package com.municipality.backend.domain.model.user

import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId

class AnonymousUser : User<AnonymousUserId> {
    override fun id() = AnonymousUserId.instance

    override fun name() = ""

    override fun isAdmin() = false

    override fun isRegistered() = false

    override fun isSystem() = false

    override fun isAnonymous() = true

    override fun isResponsible(municipalityId: MunicipalityId) = false

    override fun isResponsible(districtId: DistrictId) = false

    override fun isAuditor(municipalityId: MunicipalityId) = false

    override fun isAuditor(districtId: DistrictId) = false

    override fun isMunicipalityResponsible() = false

    override fun isMunicipalityAuditor() = false

    override fun isDistrictResponsible() = false

    override fun isDistrictAuditor() = false
}