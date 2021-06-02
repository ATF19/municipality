package com.municipality.backend.infrastructure.persistence.repository

import com.municipality.backend.domain.model.file.File
import com.municipality.backend.domain.model.file.FileId
import com.municipality.backend.domain.service.file.Files
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class FileRepository(
    private val fileJpaRepository: FileJpaRepository
) : Files {
    override fun save(file: File) {
        fileJpaRepository.save(file)
    }

    override fun by(fileId: FileId): File = fileJpaRepository
        .findById(fileId)
        .orElseThrow { NoSuchElementException("No file exists with the ID '${fileId.rawId}'") }
}

interface FileJpaRepository : JpaRepository<File, FileId>