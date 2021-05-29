package com.municipality.backend.infrastructure.mapping

import com.municipality.backend.domain.model.core.Id
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.user.role.*
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import kotlin.streams.toList

private const val ROLE_ID_SEPARATOR = "|"
private const val ROLES_SEPARATOR = ";"
private const val ADMIN_ROLE = "ADMIN"
private const val MUNICIPALITY_RESPONSIBLE_ROLE = "MUNICIPALITY_RESPONSIBLE"
private const val MUNICIPALITY_AUDITOR_ROLE = "MUNICIPALITY_AUDITOR"
private const val DISTRICT_RESPONSIBLE_ROLE = "DISTRICT_RESPONSIBLE"
private const val DISTRICT_AUDITOR_ROLE = "DISTRICT_AUDITOR"

@Converter(autoApply = true)
class UserRolesConverter : AttributeConverter<Roles, String> {

    override fun convertToDatabaseColumn(roles: Roles?): String {
        if (roles == null)
            return ""

        return roles.all()
            .map { mapRoleToString(it) }
            .toList()
            .joinToString(ROLES_SEPARATOR)
    }

    override fun convertToEntityAttribute(serializedRoles: String?): Roles {
        if (serializedRoles == null || serializedRoles.isEmpty())
            return Roles.empty()

        val rolesList = serializedRoles
            .split(ROLES_SEPARATOR)
            .stream()
            .map { mapStringToRole(it) }
            .filter { it.isPresent }
            .map { it.get() }
            .toList()
        return Roles.of(rolesList)
    }

    private fun mapRoleToString(role: Role): String {
        return when(role) {
            is Admin -> ADMIN_ROLE
            is MunicipalityResponsible -> serialzeRole(MUNICIPALITY_RESPONSIBLE_ROLE, role.municipalityId)
            is MunicipalityAuditor -> serialzeRole(MUNICIPALITY_AUDITOR_ROLE, role.municipalityId)
            is DistrictResponsible -> serialzeRole(DISTRICT_RESPONSIBLE_ROLE, role.districtId)
            is DistrictAuditor -> serialzeRole(DISTRICT_AUDITOR_ROLE, role.districtId)
            else -> ""
        }
    }

    private fun serialzeRole(roleName: String, id: Id) = "$roleName$ROLE_ID_SEPARATOR${id.rawId}"

    private fun mapStringToRole(serializedRole: String): Optional<Role> {
        if (serializedRole == ADMIN_ROLE)
            return Optional.of(Admin())

        val splittedRoleId = serializedRole.split(ROLE_ID_SEPARATOR)
        val id = splittedRoleId[1]
        return Optional.ofNullable(
            when(splittedRoleId[0]) {
                MUNICIPALITY_RESPONSIBLE_ROLE -> MunicipalityResponsible(MunicipalityId(id))
                MUNICIPALITY_AUDITOR_ROLE -> MunicipalityAuditor(MunicipalityId(id))
                DISTRICT_RESPONSIBLE_ROLE -> DistrictResponsible(DistrictId(id))
                DISTRICT_AUDITOR_ROLE -> DistrictAuditor(DistrictId(id))
                else -> null
            }
        )
    }
}