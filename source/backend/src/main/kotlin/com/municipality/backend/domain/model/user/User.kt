package com.municipality.backend.domain.model.user

import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.municipality.district.DistrictId

interface User<T : UserId> {
    fun id() : T
    fun isAdmin(): Boolean
    fun isRegistered(): Boolean
    fun isSystem(): Boolean
    fun isAnonymous(): Boolean
    fun isResponsible(municipalityId: MunicipalityId): Boolean
    fun isResponsible(districtId: DistrictId): Boolean
    fun isAuditor(municipalityId: MunicipalityId): Boolean
    fun isAuditor(districtId: DistrictId): Boolean
}