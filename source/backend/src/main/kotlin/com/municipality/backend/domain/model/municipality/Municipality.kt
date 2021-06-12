package com.municipality.backend.domain.model.municipality

import com.municipality.backend.domain.model.core.DomainEntity
import com.municipality.backend.domain.model.district.District
import javax.persistence.*

@Entity
class Municipality : DomainEntity<MunicipalityId> {

    lateinit var name: MunicipalityName
    lateinit var nameInArabic: MunicipalityNameInArabic

    @OneToMany(
        mappedBy = "municipality",
        cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER
    )
    var districts: List<District> = mutableListOf()

    constructor() : super(MunicipalityId())
    constructor(id: MunicipalityId) : super(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Municipality

        if (name != other.name) return false
        if (nameInArabic != other.nameInArabic) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + nameInArabic.hashCode()
        return result
    }

    override fun toString(): String {
        return "Municipality(name=$name, nameInArabic=$nameInArabic)"
    }
}

@Embeddable
data class MunicipalityName(val name: String? = null)

@Embeddable
data class MunicipalityNameInArabic(val nameInArabic: String? = null)