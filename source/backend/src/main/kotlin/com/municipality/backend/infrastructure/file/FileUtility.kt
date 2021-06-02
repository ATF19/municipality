package com.municipality.backend.infrastructure.file

import com.municipality.backend.domain.model.file.ContentType
import com.municipality.backend.domain.model.file.File
import com.municipality.backend.domain.model.file.FileName
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

@Component
class FileUtility {
    fun create(file: MultipartFile): File {
        val name = if (file.originalFilename != null)
            FileName(StringUtils.cleanPath(file.originalFilename!!))
        else
            FileName("Random_" + UUID.randomUUID().toString())
        val internalFile = File()
        internalFile.fileName = name
        if (file.contentType != null)
            internalFile.contentType = ContentType(file.contentType)
        internalFile.blob = file.bytes
        return internalFile
    }

    fun asUrl(file: File): String = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/file/")
            .path(file.id.rawId.toString())
            .toUriString()
}