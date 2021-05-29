package com.municipality.backend.infrastructure.persistence.repository.municipality

import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.municipality.MunicipalityBuilder
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.service.municipality.Municipalities
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test


class MunicipalityRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var municipalities: Municipalities

    @Autowired
    private lateinit var municipalityJpaRepository: MunicipalityJpaRepository

    @Test(groups = [TestGroup.INTEGRATION])
    fun create_municipality() {
        // given
        val municipality = MunicipalityBuilder().build()

        // when
        municipalities.create(municipality)

        // then
        assertThat(municipalityJpaRepository.findById(municipality.id)).contains(municipality)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun throw_exception_if_id_was_not_found() {
        // given

        // when

        // then
        Assertions.assertThatThrownBy { municipalities.by(MunicipalityId()) }
            .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_by_id() {
        // given
        val municipality = MunicipalityBuilder().build()
        municipalities.create(municipality)

        // when
        val result = municipalities.by(municipality.id)

        // then
        assertThat(result).isEqualTo(municipality)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_all_by_ids() {
        // given
        municipalityJpaRepository.deleteAll()
        val municipality = MunicipalityBuilder().build()
        municipalities.create(municipality)

        // when
        val result = municipalities.by(setOf(municipality.id), PageNumber(1), DEFAULT_PAGE_SIZE)

        // then
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.pageNumber.number).isEqualTo(1)
        assertThat(result.elements).containsOnly(municipality)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_all() {
        // given
        municipalityJpaRepository.deleteAll()
        val municipality = MunicipalityBuilder().build()
        municipalities.create(municipality)

        // when
        val result = municipalities.all(PageNumber(1), DEFAULT_PAGE_SIZE)

        // then
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.pageNumber.number).isEqualTo(1)
        assertThat(result.elements).containsOnly(municipality)
    }
}