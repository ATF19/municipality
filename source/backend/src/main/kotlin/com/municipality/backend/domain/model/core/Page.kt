package com.municipality.backend.domain.model.core

val FIRST_PAGE = PageNumber(1)
val DEFAULT_PAGE_SIZE = PageSize(10)
data class PageNumber(val number: Int)
data class PageSize(val size: Int)