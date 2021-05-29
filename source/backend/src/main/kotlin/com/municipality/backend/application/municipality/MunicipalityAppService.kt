package com.municipality.backend.application.municipality

import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.municipality.Municipality
import com.municipality.backend.domain.model.user.RegisteredUser
import com.municipality.backend.domain.model.user.User
import com.municipality.backend.domain.service.municipality.Municipalities
import org.springframework.stereotype.Component

@Component
class MunicipalityAppService(
    private val municipalities: Municipalities
) {
    fun all(user: User<*>, pageNumber: PageNumber, pageSize: PageSize): Page<Municipality> {
        if (!user.isAdmin() && !user.isMunicipalityResponsible() && !user.isMunicipalityAuditor())
            throw InsufficientPermissionException()

        if (user.isAdmin())
            return municipalities.all(pageNumber, pageSize)

        user as RegisteredUser
        return municipalities.by(user.roles.municipalities(), pageNumber, pageSize)
    }
}