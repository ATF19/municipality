package com.municipality.backend.domain.service.file

import com.municipality.backend.domain.model.file.File
import com.municipality.backend.domain.model.file.FileId

interface Files {
    fun save(file: File)
    fun by(fileId: FileId): File
}