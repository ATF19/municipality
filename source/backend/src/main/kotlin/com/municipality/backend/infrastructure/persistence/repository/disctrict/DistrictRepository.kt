package com.municipality.backend.infrastructure.persistence.repository.disctrict

import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.district.District
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.service.district.Districts
import com.municipality.backend.infrastructure.persistence.repository.PageBuilder
import org.springframework.stereotype.Repository

@Repository
class DistrictRepository(
    private val districtJpaRepository: DistrictJpaRepository
) : Districts {

    override fun by(id: DistrictId): District = districtJpaRepository
        .findById(id)
        .orElseThrow { NoSuchElementException("No district exists with the ID '${id.rawId}'") }

    override fun by(
        ids: Set<DistrictId>,
        municipalityIds: Set<MunicipalityId>,
        pageNumber: PageNumber,
        pageSize: PageSize
    ): Page<District> {
        val byIds =
            districtJpaRepository.findByIds(ids, municipalityIds, PageBuilder.builder.build(pageNumber, pageSize))
        return Page(byIds.content, pageNumber, pageSize, byIds.totalPages)
    }

    override fun all(pageNumber: PageNumber, pageSize: PageSize): Page<District> {
        val all = districtJpaRepository.findAll(PageBuilder.builder.build(pageNumber, pageSize))
        return Page(all.content, pageNumber, pageSize, all.totalPages)
    }
}