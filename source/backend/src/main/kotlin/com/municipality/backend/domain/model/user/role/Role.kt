package com.municipality.backend.domain.model.user.role

import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId

interface Role

class Admin : Role {
    override fun equals(other: Any?) = other is Admin
    override fun hashCode() = "ADMIN".hashCode()
    override fun toString() = "ADMIN"
}

data class MunicipalityResponsible(val municipalityId: MunicipalityId) : Role

data class MunicipalityAuditor(val municipalityId: MunicipalityId) : Role

data class DistrictResponsible(val districtId: DistrictId) : Role

data class DistrictAuditor(val districtId: DistrictId) : Role