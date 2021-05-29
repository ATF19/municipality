package com.municipality.backend.domain.model.user

import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId

interface User<T : UserId> {
    fun id(): T
    fun name(): String
    fun isAdmin(): Boolean
    fun isRegistered(): Boolean
    fun isSystem(): Boolean
    fun isAnonymous(): Boolean
    fun isMunicipalityResponsible(): Boolean
    fun isDistrictResponsible(): Boolean
    fun isMunicipalityAuditor(): Boolean
    fun isDistrictAuditor(): Boolean
    fun isResponsible(municipalityId: MunicipalityId): Boolean
    fun isResponsible(districtId: DistrictId): Boolean
    fun isAuditor(municipalityId: MunicipalityId): Boolean
    fun isAuditor(districtId: DistrictId): Boolean
}