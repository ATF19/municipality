package com.municipality.backend.infrastructure.persistence

import com.municipality.backend.domain.model.core.DummyEntity
import com.municipality.backend.domain.model.core.DummyName
import com.municipality.backend.domain.model.core.DummyNumber
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.envers.AuditReader
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

class PersistenceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var repository: DummyEntityJpaRepository

    @Autowired
    private lateinit var auditReader: AuditReader


    @Test(groups = [TestGroup.INTEGRATION])
    fun save_new_dummy_entity() {
        // given
        val dummyEntity = DummyEntity()
        dummyEntity.dummyName = DummyName("atef")
        dummyEntity.dummyNumber = DummyNumber(20)

        // when
        repository.save(dummyEntity)

        // then
        val result = repository.getById(dummyEntity.id)
        assertThat(result).isEqualTo(dummyEntity)
        assertThat(result.version).isEqualTo(1)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun update_dummy_entity() {
        // given
        val dummyEntity = DummyEntity()
        dummyEntity.dummyName = DummyName("atef")
        dummyEntity.dummyNumber = DummyNumber(20)
        repository.save(dummyEntity)
        val entityFromDb = repository.getById(dummyEntity.id)
        entityFromDb.dummyName = DummyName("New Name")
        entityFromDb.dummyNumber = DummyNumber(40)

        // when
        repository.save(entityFromDb)

        // then
        val result = repository.getById(dummyEntity.id)
        assertThat(result).isEqualTo(entityFromDb)
        assertThat(result.version).isEqualTo(2)
        assertThat(result.modifiedAt).isNotNull
        assertThat(result.modifiedAt).isNotEqualTo(result.createdAt)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun can_recover_dummy_entity_from_a_specific_version() {
        // given
        val dummyEntity = DummyEntity()
        dummyEntity.dummyName = DummyName("atef")
        dummyEntity.dummyNumber = DummyNumber(20)
        repository.save(dummyEntity) // V1

        val entityFromDb = repository.getById(dummyEntity.id)
        entityFromDb.dummyName = DummyName("New Name")
        entityFromDb.dummyNumber = DummyNumber(40)
        repository.save(entityFromDb) // V2

        // when
        val v1 = auditReader.find(DummyEntity::class.java, dummyEntity.id, 1)
        val v2 = auditReader.find(DummyEntity::class.java, dummyEntity.id, 2)

        // then
        assertThat(v1.version).isEqualTo(1)
        assertThat(v1.dummyName).isEqualTo(DummyName("atef"))
        assertThat(v1.dummyNumber).isEqualTo(DummyNumber(20))
        assertThat(v2.version).isEqualTo(2)
        assertThat(v2.dummyName).isEqualTo(DummyName("New Name"))
        assertThat(v2.dummyNumber).isEqualTo(DummyNumber(40))
    }
}