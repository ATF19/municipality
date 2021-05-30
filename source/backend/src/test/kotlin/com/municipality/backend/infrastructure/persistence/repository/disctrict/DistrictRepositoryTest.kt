package com.municipality.backend.infrastructure.persistence.repository.disctrict

import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.district.DistrictBuilder
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.service.district.Districts
import com.municipality.backend.domain.service.municipality.Municipalities
import com.municipality.backend.infrastructure.persistence.repository.municipality.MunicipalityJpaRepository
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

class DistrictRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var districts: Districts

    @Autowired
    private lateinit var municipalities: Municipalities

    @Autowired
    private lateinit var municipalityJpaRepository: MunicipalityJpaRepository

    @Autowired
    private lateinit var districtJpaRepository: DistrictJpaRepository

    @Test(groups = [TestGroup.INTEGRATION])
    fun throw_exception_if_id_was_not_found() {
        // given

        // when

        // then
        Assertions.assertThatThrownBy { districts.by(DistrictId()) }
            .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_by_id() {
        // given
        val district = DistrictBuilder().build()
        municipalities.create(district.municipality)
        districtJpaRepository.save(district)

        // when
        val result = districts.by(district.id)

        // then
        Assertions.assertThat(result).isEqualTo(district)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_all_by_ids() {
        // given
        districtJpaRepository.deleteAll()
        val district = DistrictBuilder().build()
        municipalities.create(district.municipality)
        districtJpaRepository.save(district)

        // when
        val result = districts.by(setOf(district.id), emptySet(), PageNumber(1), DEFAULT_PAGE_SIZE)

        // then
        Assertions.assertThat(result.totalPages).isEqualTo(1)
        Assertions.assertThat(result.pageNumber.number).isEqualTo(1)
        Assertions.assertThat(result.elements.map { it.id }).containsOnly(district.id)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_all() {
        // given
        municipalityJpaRepository.deleteAll()
        val district = DistrictBuilder().build()
        municipalities.create(district.municipality)
        districtJpaRepository.save(district)

        // when
        val result = districts.all(PageNumber(1), DEFAULT_PAGE_SIZE)

        // then
        Assertions.assertThat(result.totalPages).isEqualTo(1)
        Assertions.assertThat(result.pageNumber.number).isEqualTo(1)
        Assertions.assertThat(result.elements.map { it.id }).containsOnly(district.id)
    }
}