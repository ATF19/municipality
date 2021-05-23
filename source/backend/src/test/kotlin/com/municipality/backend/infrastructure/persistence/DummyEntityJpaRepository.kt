package com.municipality.backend.infrastructure.persistence

import com.municipality.backend.domain.model.core.DummyEntity
import com.municipality.backend.domain.model.core.DummyId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DummyEntityJpaRepository : JpaRepository<DummyEntity, DummyId>