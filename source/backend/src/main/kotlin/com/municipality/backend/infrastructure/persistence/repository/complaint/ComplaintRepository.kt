package com.municipality.backend.infrastructure.persistence.repository.complaint

import com.municipality.backend.domain.model.complaint.Complaint
import com.municipality.backend.domain.model.complaint.ComplaintCode
import com.municipality.backend.domain.model.complaint.ComplaintId
import com.municipality.backend.domain.model.complaint.Status
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.service.complaint.Complaints
import com.municipality.backend.infrastructure.persistence.repository.PageBuilder
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ComplaintRepository(
    private val complaintJpaRepository: ComplaintJpaRepository
) : Complaints {

    override fun exists(code: ComplaintCode): Boolean = complaintJpaRepository.existsByCode(code)

    override fun create(complaint: Complaint) {
        complaintJpaRepository.save(complaint)
    }

    override fun all(
        municipalities: Set<MunicipalityId>,
        districts: Set<DistrictId>,
        pageNumber: PageNumber,
        pageSize: PageSize
    ): Page<Complaint> {
        val status = setOf(Status.CREATED, Status.FINISHED, Status.IN_PROGRESS, Status.SENT_TO_EXTERNAL_SERVICE)
        return byStatus(status, pageNumber, pageSize)
    }

    override fun rejected(
        municipalities: Set<MunicipalityId>,
        districts: Set<DistrictId>,
        pageNumber: PageNumber,
        pageSize: PageSize
    ): Page<Complaint> = byStatus(setOf(Status.REJECTED), pageNumber, pageSize)

    override fun by(complaintId: ComplaintId): Complaint = complaintJpaRepository
        .findById(complaintId)
        .orElseThrow { NoSuchElementException("No complaint exists with the ID '${complaintId.rawId}'") }

    override fun by(complaintIds: List<ComplaintId>, pageNumber: PageNumber, pageSize: PageSize): Page<Complaint> {
        val page = PageBuilder.builder.build(pageNumber, pageSize)
        val complaints = complaintJpaRepository.findAllByIdIn(complaintIds, page)
        return Page(complaints.content, pageNumber, pageSize, complaints.totalPages)
    }

    override fun by(complaintCode: ComplaintCode): Complaint = complaintJpaRepository
        .findFirstByCode(complaintCode)
        .orElseThrow { NoSuchElementException("No complaint exists with the code '${complaintCode.code}'") }

    override fun update(complaint: Complaint) {
        complaintJpaRepository.save(complaint)
    }

    private fun byStatus(
        status: Set<Status>,
        pageNumber: PageNumber,
        pageSize: PageSize
    ): Page<Complaint> {
        val page = PageBuilder.builder.build(pageNumber, pageSize)
        val complaints = complaintJpaRepository.findAllByStatusIn(status, page)
        return Page(complaints.content, pageNumber, pageSize, complaints.totalPages)
    }
}

interface ComplaintJpaRepository : JpaRepository<Complaint, ComplaintId> {
    fun existsByCode(complaintCode: ComplaintCode): Boolean
    fun findAllByStatusIn(status: Set<Status>, pageable: Pageable): org.springframework.data.domain.Page<Complaint>
    fun findAllByIdIn(
        complaintIds: List<ComplaintId>,
        pageable: Pageable
    ): org.springframework.data.domain.Page<Complaint>

    fun findFirstByCode(complaintCode: ComplaintCode): Optional<Complaint>
}