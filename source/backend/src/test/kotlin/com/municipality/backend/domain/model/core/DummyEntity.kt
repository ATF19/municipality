package com.municipality.backend.domain.model.core

import org.hibernate.envers.Audited
import javax.persistence.Entity

@Entity
@Audited
class DummyEntity: DomainEntity<DummyId>(DummyId()) {
    lateinit var dummyName: DummyName
    lateinit var dummyNumber: DummyNumber

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as DummyEntity

        if (dummyName != other.dummyName) return false
        if (dummyNumber != other.dummyNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + dummyName.hashCode()
        result = 31 * result + dummyNumber.hashCode()
        return result
    }

    override fun toString(): String {
        return "DummyEntity(dummyName=$dummyName, dummyNumber=$dummyNumber)"
    }
}