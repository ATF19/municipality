package com.municipality.backend.application.district

import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.district.District
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.User
import com.municipality.backend.domain.service.district.Districts
import org.springframework.stereotype.Component

@Component
class DistrictAppService(
    private val districts: Districts
) {
    fun all(user: User<*>, pageNumber: PageNumber, pageSize: PageSize): Page<District> {
        if (!user.isAdmin() && !user.isMunicipalityResponsible() && !user.isMunicipalityAuditor()
            && !user.isDistrictResponsible() && !user.isDistrictAuditor())
            throw InsufficientPermissionException()

        if (user.isAdmin())
            return districts.all(pageNumber, pageSize)

        user as RegisteredUser
        return districts.by(user.roles.districts(), user.roles.municipalities(), pageNumber, pageSize)
    }
}