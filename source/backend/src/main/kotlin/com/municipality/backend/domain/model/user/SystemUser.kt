package com.municipality.backend.domain.model.user

import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.municipality.district.DistrictId

class SystemUser : User<SystemUserId> {
    override fun id() = SystemUserId.instance

    override fun name() = "SYS--_"

    override fun isAdmin() = false

    override fun isRegistered() = false

    override fun isSystem() = true

    override fun isAnonymous() = false

    override fun isResponsible(municipalityId: MunicipalityId) = false

    override fun isResponsible(districtId: DistrictId) = false

    override fun isAuditor(municipalityId: MunicipalityId) = false

    override fun isAuditor(districtId: DistrictId) = false
}