package com.municipality.backend.domain.service.district

import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.district.District
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId

interface Districts {
    fun by(id: DistrictId): District
    fun by(
        ids: Set<DistrictId>,
        municipalityIds: Set<MunicipalityId>,
        pageNumber: PageNumber,
        pageSize: PageSize
    ): Page<District>

    fun all(pageNumber: PageNumber, pageSize: PageSize): Page<District>
}