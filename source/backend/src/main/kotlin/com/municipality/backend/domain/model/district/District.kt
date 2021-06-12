package com.municipality.backend.domain.model.district

import com.municipality.backend.domain.model.complaint.Complaint
import com.municipality.backend.domain.model.core.DomainEntity
import com.municipality.backend.domain.model.municipality.Municipality
import javax.persistence.*

@Entity
class District : DomainEntity<DistrictId> {

    lateinit var name: DistrictName
    lateinit var nameInArabic: DistrictNameInArabic

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "municipality_id", nullable = false)
    lateinit var municipality: Municipality

    @OneToMany(
        mappedBy = "district",
        cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER
    )
    var complaints: List<Complaint> = mutableListOf()

    constructor() : super(DistrictId())
    constructor(id: DistrictId) : super(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as District

        if (name != other.name) return false
        if (nameInArabic != other.nameInArabic) return false
        if (municipality != other.municipality) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + if (this::name.isInitialized) name.hashCode() else 0
        result = 31 * result + if (this::nameInArabic.isInitialized) nameInArabic.hashCode() else 0
        result = 31 * result + if (this::municipality.isInitialized) municipality.hashCode() else 0
        return result
    }

    override fun toString(): String {
        return "District(name=$name, nameInArabic=$nameInArabic)"
    }
}

@Embeddable
data class DistrictName(val name: String? = null)

@Embeddable
data class DistrictNameInArabic(val nameInArabic: String? = null)
