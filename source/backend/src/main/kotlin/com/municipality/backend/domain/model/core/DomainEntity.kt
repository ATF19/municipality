package com.municipality.backend.domain.model.core

import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class DomainEntity<T : Id>(
    @EmbeddedId
    val id: T
) {

    @Version
    @Audited
    var version: Long = 1

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Audited
    var createdAt: Instant? = null

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    @Audited
    var modifiedAt: Instant? = null

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    @Audited
    var createdBy: String? = null

    @LastModifiedBy
    @Column(name = "updated_by", nullable = true)
    @Audited
    var updatedBy: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DomainEntity<*>

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "DomainEntity(id=$id, version=$version, createdAt=$createdAt, modifiedAt=$modifiedAt)"
    }
}