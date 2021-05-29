package com.municipality.backend.infrastructure.persistence.repository.disctrict

import com.municipality.backend.domain.model.district.District
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface DistrictJpaRepository : JpaRepository<District, DistrictId> {
    @Query("select d from District d where id in :ids or municipality_id in :municipalityIds")
    fun findByIds(@Param("ids") ids: Set<DistrictId>, @Param("municipalityIds") municipalityIds: Set<MunicipalityId>, pageRequest: Pageable): Page<District>
}