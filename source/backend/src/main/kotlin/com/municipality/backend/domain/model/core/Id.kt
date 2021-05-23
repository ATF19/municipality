package com.municipality.backend.domain.model.core

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class Id : Serializable {
    @Column(name = "id")
    val rawId: UUID

    constructor() {
        rawId = UUID.randomUUID()
    }

    constructor(uuid: UUID) {
        rawId = uuid
    }

    constructor(rawUuid: String) {
        rawId = UUID.fromString(rawUuid)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Id

        if (rawId != other.rawId) return false

        return true
    }

    override fun hashCode(): Int {
        return rawId.hashCode()
    }

    override fun toString(): String {
        return "Id(rawId=$rawId)"
    }
}
