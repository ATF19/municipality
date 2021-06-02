package com.municipality.backend.domain.model.complaint

import com.municipality.backend.domain.model.core.DomainEntity
import com.municipality.backend.domain.model.district.District
import com.municipality.backend.domain.model.file.File
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.FirstName
import com.municipality.backend.domain.model.user.LastName
import javax.persistence.*

@Entity
class Complaint : DomainEntity<ComplaintId> {

    @Column(unique = true)
    lateinit var code: ComplaintCode
    var comment: Comment? = null
    lateinit var address: Address
    var personalInfo: PersonalInfo? = null
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    lateinit var picture: File
    var position: Position? = null
    @Enumerated(EnumType.STRING)
    lateinit var status: Status
    var resultComment: ResultComment? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="district_id", nullable=false)
    lateinit var district: District

    constructor() : super(ComplaintId())
    constructor(id: ComplaintId) : super(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Complaint

        if (code != other.code) return false
        if (comment != other.comment) return false
        if (address != other.address) return false
        if (personalInfo != other.personalInfo) return false
        if (picture != other.picture) return false
        if (position != other.position) return false
        if (status != other.status) return false
        if (resultComment != other.resultComment) return false
        if (district != other.district) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + comment.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + personalInfo.hashCode()
        result = 31 * result + picture.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + resultComment.hashCode()
        result = 31 * result + district.hashCode()
        return result
    }

    override fun toString(): String {
        return "Complaint(code=$code, comment=$comment, address=$address, personalInfo=$personalInfo, picture=$picture, position=$position, status=$status, resultComment=$resultComment, district=$district)"
    }
}



@Embeddable
data class ComplaintCode(val code: String? = null)

@Embeddable
data class Comment(val comment: String? = null)

@Embeddable
data class Address(val address: String? = null)

@Embeddable
data class PersonalInfo(val firstName: FirstName? = null, val lastName: LastName? = null,
                        val phone: Phone? = null, val email: Email? = null)

@Embeddable
data class Phone(val phoneNumber: String? = null)

@Embeddable
data class Position(val longitude: Double? = null, val latitude: Double? = null)

@Embeddable
data class ResultComment(val resultComment: String? = null)

enum class Status {
    CREATED, IN_PROGRESS, SENT_TO_EXTERNAL_SERVICE, FINISHED, REJECTED
}