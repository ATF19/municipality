package com.municipality.backend.domain.service.municipality

import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.municipality.Municipality
import com.municipality.backend.domain.model.municipality.MunicipalityId

interface Municipalities {
    fun create(municipality: Municipality)
    fun by(municipalityId: MunicipalityId): Municipality
    fun by(municipalityIds: Set<MunicipalityId>, pageNumber: PageNumber, pageSize: PageSize): Page<Municipality>
    fun all(pageNumber: PageNumber, pageSize: PageSize): Page<Municipality>
}