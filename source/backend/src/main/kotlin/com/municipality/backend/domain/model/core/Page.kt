package com.municipality.backend.domain.model.core

val FIRST_PAGE = PageNumber(1)
val DEFAULT_PAGE_SIZE = PageSize(10)

data class PageNumber(val number: Int)
data class PageSize(val size: Int)
data class Page<T>(val elements: List<T>, val pageNumber: PageNumber, val pageSize: PageSize, val totalPages: Int)