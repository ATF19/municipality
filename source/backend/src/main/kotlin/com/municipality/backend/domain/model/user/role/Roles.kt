package com.municipality.backend.domain.model.user.role

import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.municipality.district.DistrictId

class Roles private constructor(private var listOfRoles: MutableSet<Role>) {

    companion object {
        fun empty() = Roles(mutableSetOf())
        fun of(vararg role: Role) = Roles(role.toMutableSet())
        fun of(role: List<Role>) = Roles(role.toMutableSet())
    }

    fun grant(role: Role) {
        listOfRoles.add(role)
    }

    fun revoke(role: Role) {
        listOfRoles.remove(role)
    }

    fun all() = listOfRoles.stream()

    fun isAdmin() = listOfRoles.contains(Admin())

    fun isMunicipalityResponsible(municipalityId: MunicipalityId) =
        listOfRoles.contains(MunicipalityResponsible(municipalityId))

    fun isMunicipalityAuditor(municipalityId: MunicipalityId) =
        listOfRoles.contains(MunicipalityAuditor(municipalityId))

    fun isDistrictResponsible(districtId: DistrictId) =
        listOfRoles.contains(DistrictResponsible(districtId))

    fun isDistrictAuditor(districtId: DistrictId) =
        listOfRoles.contains(DistrictAuditor(districtId))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Roles

        if (listOfRoles != other.listOfRoles) return false

        return true
    }

    override fun hashCode(): Int {
        return listOfRoles.hashCode()
    }
}