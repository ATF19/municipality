package com.municipality.backend.domain.model.complaint

import com.municipality.backend.domain.model.district.DistrictBuilder

class ComplaintBuilder {
    var id = ComplaintId()
    var code = ComplaintCode(id.rawId.toString())
    var comment = Comment("Complaint comment")
    var address = Address("21 bis rue de la Tripe")
    var personalInfo = PersonalInfo(null, null, null, null)
    var picture = Picture("http://demo.com/img.png")
    var position = Position(null, null)
    var status = Status.CREATED
    var resultComment = ResultComment(null)
    var district = DistrictBuilder().build()

    fun build(): Complaint {
        val complaint = Complaint(id)
        complaint.code = code
        complaint.comment = comment
        complaint.address = address
        complaint.personalInfo = personalInfo
        complaint.picture = picture
        complaint.position = position
        complaint.status = status
        complaint.resultComment = resultComment
        complaint.district = district
        return complaint
    }

}