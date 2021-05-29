package com.municipality.backend.infrastructure.persistence.repository.municipality

import com.municipality.backend.domain.model.municipality.Municipality
import com.municipality.backend.domain.model.municipality.MunicipalityId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MunicipalityJpaRepository : JpaRepository<Municipality, MunicipalityId> {
    @Query("select m from Municipality m where id in :ids")
    fun findByIds(@Param("ids") ids: Set<MunicipalityId>, pageRequest: Pageable): Page<Municipality>
}