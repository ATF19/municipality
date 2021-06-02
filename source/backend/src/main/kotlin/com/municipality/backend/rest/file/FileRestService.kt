package com.municipality.backend.rest.file

import com.municipality.backend.domain.model.file.FileId
import com.municipality.backend.domain.service.file.Files
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/file")
@Transactional
class FileRestService(
    private val files: Files
) {
    @GetMapping("{fileId}", produces = [MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE])
    fun fileById(@PathVariable("fileId") id: String): ByteArray =
        files.by(FileId(id)).blob
}