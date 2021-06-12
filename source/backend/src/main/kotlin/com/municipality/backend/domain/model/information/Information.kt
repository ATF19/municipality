package com.municipality.backend.domain.model.information

import com.municipality.backend.domain.model.complaint.Phone
import com.municipality.backend.domain.model.core.DomainEntity
import com.municipality.backend.domain.model.user.Email
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity

@Entity
class Information : DomainEntity<InformationId>(InformationId.DEFAULT) {

    @Column(name = "intro", columnDefinition = "TEXT")
    lateinit var intro: Intro
    lateinit var phone: Phone
    lateinit var email: Email

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Information

        if (intro != other.intro) return false
        if (phone != other.phone) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + intro.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }

    override fun toString(): String {
        return "Information(intro=$intro, phone=$phone, email=$email)"
    }
}

@Embeddable
data class Intro(val intro: String? = null)