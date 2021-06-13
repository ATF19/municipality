package com.municipality.backend.domain.model.news

import java.time.Instant

data class ArticleWithoutContent(val id: ArticleId, val title: Title,
                                 val createdAt: Instant? = null, val createdBy: String? = null)
