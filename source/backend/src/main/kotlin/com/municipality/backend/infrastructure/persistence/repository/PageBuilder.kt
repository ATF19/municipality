package com.municipality.backend.infrastructure.persistence.repository

import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PageBuilder private constructor() {
    companion object {
        val builder = PageBuilder()
    }

    fun build(pageNumber: PageNumber, pageSize: PageSize) = PageRequest
        .of(pageNumber.number - 1, pageSize.size, Sort.by("createdAt").descending())
}