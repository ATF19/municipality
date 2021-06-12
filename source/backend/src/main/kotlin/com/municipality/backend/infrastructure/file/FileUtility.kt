package com.municipality.backend.infrastructure.file

import com.municipality.backend.domain.model.file.ContentType
import com.municipality.backend.domain.model.file.File
import com.municipality.backend.domain.model.file.FileName
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

@Component
class FileUtility {
    fun create(base64: String, contentType: ContentType): File {
        val internalFile = File()
        internalFile.fileName = FileName("Random_" + UUID.randomUUID().toString())
        internalFile.contentType = contentType
        internalFile.blob = Base64Utils.decodeFromString(base64)
        return internalFile
    }

    fun asUrl(file: File): String = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/file/")
        .path(file.id.rawId.toString())
        .toUriString()
}