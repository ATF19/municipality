package com.municipality.backend.rest.core

import com.municipality.backend.domain.model.core.Page

class PageDto<in T, out D>(page: Page<T>, mapper: (T) -> D) {
    val number = page.pageNumber.number
    val size = page.pageSize.size
    val totalPages = page.totalPages
    val elements: List<D> = page.elements.map { mapper.invoke(it) }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PageDto<*, *>

        if (number != other.number) return false
        if (size != other.size) return false
        if (totalPages != other.totalPages) return false
        if (elements != other.elements) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + size
        result = 31 * result + totalPages
        result = 31 * result + elements.hashCode()
        return result
    }

    override fun toString(): String {
        return "PageDto(number=$number, size=$size, totalPages=$totalPages, elements=$elements)"
    }
}