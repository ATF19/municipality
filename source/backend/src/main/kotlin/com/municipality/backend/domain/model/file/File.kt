package com.municipality.backend.domain.model.file

import com.municipality.backend.domain.model.core.DomainEntity
import javax.persistence.*

@Entity
class File : DomainEntity<FileId> {

    lateinit var fileName: FileName
    var contentType: ContentType? = null
    @Lob
    lateinit var blob: ByteArray

    constructor() : super(FileId())
    constructor(id: FileId) : super(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as File

        if (fileName != other.fileName) return false
        if (contentType != other.contentType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + contentType.hashCode()
        return result
    }

    override fun toString(): String {
        return "File(fileName=$fileName, contentType=$contentType)"
    }
}

@Embeddable
data class FileName(val name: String? = null)

@Embeddable
data class ContentType(val type: String? = null)
