package com.municipality.backend.application.user

import com.municipality.backend.application.Command
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.municipality.district.DistrictId
import com.municipality.backend.domain.model.user.*
import java.util.*

data class RegisterInternalUserCommand(
    override val user: User<*>,
    val username: Username,
    val email: Email,
    val unencryptedPassword: UnencryptedPassword,
    val firstName: FirstName,
    val lastName: LastName,
    val isAdmin: Boolean,
    val municipalitiesResponsibleFor: Set<MunicipalityId>,
    val municipalitiesAuditorFor: Set<MunicipalityId>,
    val districtsResponsibleFor: Set<DistrictId>,
    val districtsAuditorFor: Set<DistrictId>
) : Command()

data class UpdateProfileCommand(
    override val user: User<*>,
    val email: Email,
    val unencryptedPassword: Optional<UnencryptedPassword>,
    val firstName: FirstName,
    val lastName: LastName
) : Command()