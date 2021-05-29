package com.municipality.backend.rest.core

import com.municipality.backend.domain.model.core.Page

class PageDto<in T, out D>(page: Page<T>, mapper: (T) -> D) {
    val number = page.pageNumber.number
    val size = page.pageSize.size
    val totalPages = page.totalPages
    val elements: List<D> = page.elements.map { mapper.invoke(it) }
}