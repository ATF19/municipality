package com.municipality.backend.domain.model.user.role

import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId

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

    fun isMunicipalityResponsible() =
        listOfRoles.any { role -> role is MunicipalityResponsible }

    fun isMunicipalityAuditor() =
        listOfRoles.any { role -> role is MunicipalityAuditor }

    fun isDistrictResponsible() =
        listOfRoles.any { role -> role is DistrictResponsible }

    fun isDistrictAuditor() =
        listOfRoles.any { role -> role is DistrictAuditor }

    fun municipalities(): Set<MunicipalityId> {
        val municipalities = HashSet<MunicipalityId>()
        listOfRoles.forEach { role ->
            run {
                when (role) {
                    is MunicipalityResponsible -> municipalities.add(role.municipalityId)
                    is MunicipalityAuditor -> municipalities.add(role.municipalityId)
                    else -> {
                    }
                }
            }
        }
        return municipalities
    }

    fun districts(): Set<DistrictId> {
        val districts = HashSet<DistrictId>()
        listOfRoles.forEach { role ->
            run {
                when (role) {
                    is DistrictResponsible -> districts.add(role.districtId)
                    is DistrictAuditor -> districts.add(role.districtId)
                    else -> {
                    }
                }
            }
        }
        return districts
    }

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