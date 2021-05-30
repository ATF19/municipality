package com.municipality.backend.domain.service.complaint

import com.municipality.backend.domain.model.complaint.Complaint
import com.municipality.backend.domain.model.complaint.ComplaintCode
import com.municipality.backend.domain.model.complaint.ComplaintId
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId

interface Complaints {
    fun exists(code: ComplaintCode): Boolean
    fun create(complaint: Complaint)
    fun all(municipalities: Set<MunicipalityId>, districts: Set<DistrictId>,
            pageNumber: PageNumber, pageSize: PageSize): Page<Complaint>
    fun rejected(municipalities: Set<MunicipalityId>, districts: Set<DistrictId>,
            pageNumber: PageNumber, pageSize: PageSize): Page<Complaint>
    fun by(complaintId: ComplaintId): Complaint
    fun by(complaintCode: ComplaintCode): Complaint
    fun update(complaint: Complaint)
}