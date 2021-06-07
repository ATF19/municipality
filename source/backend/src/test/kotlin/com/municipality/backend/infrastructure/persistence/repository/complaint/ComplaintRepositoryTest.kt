package com.municipality.backend.infrastructure.persistence.repository.complaint

import com.municipality.backend.domain.model.complaint.Comment
import com.municipality.backend.domain.model.complaint.ComplaintBuilder
import com.municipality.backend.domain.model.complaint.ComplaintCode
import com.municipality.backend.domain.model.complaint.Status
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.service.complaint.Complaints
import com.municipality.backend.infrastructure.persistence.repository.disctrict.DistrictJpaRepository
import com.municipality.backend.infrastructure.persistence.repository.municipality.MunicipalityJpaRepository
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

class ComplaintRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var complaintJpaRepository: ComplaintJpaRepository

    @Autowired
    private lateinit var municipalityJpaRepository: MunicipalityJpaRepository

    @Autowired
    private lateinit var districtJpaRepository: DistrictJpaRepository

    @Autowired
    private lateinit var complaints: Complaints

    @Test(groups = [TestGroup.INTEGRATION])
    fun code_does_not_exists() {
        // given

        // when
        val exists = complaints.exists(ComplaintCode("Dummy"))

        // then
        assertThat(exists).isFalse
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun code_exists() {
        // given
        val complaint = ComplaintBuilder().build()
        municipalityJpaRepository.save(complaint.district.municipality)
        districtJpaRepository.save(complaint.district)
        complaintJpaRepository.save(complaint)

        // when
        val exists = complaints.exists(complaint.code)

        // then
        assertThat(exists).isTrue
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun create_complaint() {
        // given
        val complaint = ComplaintBuilder().build()
        municipalityJpaRepository.save(complaint.district.municipality)
        districtJpaRepository.save(complaint.district)

        // when
        complaints.create(complaint)

        // then
        assertThat(complaintJpaRepository.findById(complaint.id).get().id).isEqualTo(complaint.id)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun retrieve_all() {
        // given
        municipalityJpaRepository.deleteAll()
        val complaint1 = ComplaintBuilder().build()
        val complaint2 = ComplaintBuilder().build()
        val complaint3 = ComplaintBuilder().build()
        val complaint4 = ComplaintBuilder().build()
        complaint1.status = Status.SENT_TO_EXTERNAL_SERVICE
        complaint2.status = Status.CREATED
        complaint3.status = Status.REJECTED
        complaint4.status = Status.IN_PROGRESS
        complaint2.district = complaint1.district
        complaint3.district = complaint1.district
        complaint4.district = complaint1.district
        municipalityJpaRepository.save(complaint1.district.municipality)
        districtJpaRepository.save(complaint1.district)
        complaints.create(complaint1)
        complaints.create(complaint2)
        complaints.create(complaint3)
        complaints.create(complaint4)

        // when
        val result = complaints.all(emptySet(), setOf(complaint1.district.id), PageNumber(1), DEFAULT_PAGE_SIZE)

        // then
        assertThat(result.elements.map {it.id}).containsExactlyInAnyOrder(complaint1.id, complaint2.id, complaint4.id)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun retrieve_rejected() {
        // given
        municipalityJpaRepository.deleteAll()
        val complaint1 = ComplaintBuilder().build()
        val complaint2 = ComplaintBuilder().build()
        val complaint3 = ComplaintBuilder().build()
        val complaint4 = ComplaintBuilder().build()
        complaint1.status = Status.REJECTED
        complaint2.status = Status.CREATED
        complaint3.status = Status.REJECTED
        complaint4.status = Status.IN_PROGRESS
        complaint2.district = complaint1.district
        complaint3.district = complaint1.district
        complaint4.district = complaint1.district
        municipalityJpaRepository.save(complaint1.district.municipality)
        districtJpaRepository.save(complaint1.district)
        complaints.create(complaint1)
        complaints.create(complaint2)
        complaints.create(complaint3)
        complaints.create(complaint4)

        // when
        val result = complaints.rejected(emptySet(), setOf(complaint1.district.id), PageNumber(1), DEFAULT_PAGE_SIZE)

        // then
        assertThat(result.elements.map {it.id}).containsExactlyInAnyOrder(complaint1.id, complaint3.id)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun retrieve_by_id() {
        // given
        val complaint = ComplaintBuilder().build()
        municipalityJpaRepository.save(complaint.district.municipality)
        districtJpaRepository.save(complaint.district)
        complaints.create(complaint)

        // when
        val result = complaints.by(complaint.id)

        // then
        assertThat(result.id).isEqualTo(complaint.id)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun retrieve_by_code() {
        // given
        val complaint = ComplaintBuilder().build()
        municipalityJpaRepository.save(complaint.district.municipality)
        districtJpaRepository.save(complaint.district)
        complaints.create(complaint)

        // when
        val result = complaints.by(complaint.code)

        // then
        assertThat(result.id).isEqualTo(complaint.id)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun update() {
        // given
        val complaint = ComplaintBuilder().build()
        municipalityJpaRepository.save(complaint.district.municipality)
        districtJpaRepository.save(complaint.district)
        complaints.create(complaint)
        val comment = Comment("new comment")
        complaint.comment = comment

        // when
        complaints.update(complaint)

        // then
        assertThat(complaints.by(complaint.id).comment).isEqualTo(comment)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_by_ids() {
        // given
        val complaint1 = ComplaintBuilder().build()
        val complaint2 = ComplaintBuilder().build()
        val complaint3 = ComplaintBuilder().build()
        municipalityJpaRepository.save(complaint1.district.municipality)
        municipalityJpaRepository.save(complaint2.district.municipality)
        municipalityJpaRepository.save(complaint3.district.municipality)
        districtJpaRepository.save(complaint1.district)
        districtJpaRepository.save(complaint2.district)
        districtJpaRepository.save(complaint3.district)
        complaints.create(complaint1)
        complaints.create(complaint2)
        complaints.create(complaint3)

        // when
        val result = complaints.by(listOf(complaint1.id, complaint2.id), PageNumber(1), DEFAULT_PAGE_SIZE)

        // then
        assertThat(result.elements.map { it.id }).containsExactlyInAnyOrder(complaint1.id, complaint2.id)
    }
}