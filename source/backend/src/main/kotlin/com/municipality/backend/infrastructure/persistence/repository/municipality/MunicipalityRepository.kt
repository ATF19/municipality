package com.municipality.backend.infrastructure.persistence.repository.municipality

import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.municipality.Municipality
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.service.municipality.Municipalities
import com.municipality.backend.infrastructure.persistence.repository.PageBuilder
import org.springframework.stereotype.Repository

@Repository
class MunicipalityRepository(
    private val municipalityJpaRepository: MunicipalityJpaRepository
) : Municipalities {

    override fun create(municipality: Municipality) {
        municipalityJpaRepository.save(municipality)
    }

    override fun by(municipalityId: MunicipalityId): Municipality = municipalityJpaRepository
        .findById(municipalityId)
        .orElseThrow { NoSuchElementException("No municipality exists with the ID '${municipalityId.rawId}'") }

    override fun by(municipalityIds: Set<MunicipalityId>, pageNumber: PageNumber, pageSize: PageSize): Page<Municipality> {
        val byIds =
            municipalityJpaRepository.findByIds(municipalityIds, PageBuilder.builder.build(pageNumber, pageSize))
        return Page(byIds.content, pageNumber, pageSize, byIds.totalPages)
    }

    override fun all(pageNumber: PageNumber, pageSize: PageSize): Page<Municipality> {
        val all = municipalityJpaRepository.findAll(PageBuilder.builder.build(pageNumber, pageSize))
        return Page(all.content, pageNumber, pageSize, all.totalPages)
    }
}