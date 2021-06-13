package com.municipality.backend.domain.service.news

import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.news.Article
import com.municipality.backend.domain.model.news.ArticleId
import com.municipality.backend.domain.model.news.ArticleWithoutContent

interface Articles {
    fun all(pageNumber: PageNumber, pageSize: PageSize): Page<ArticleWithoutContent>
    fun by(articleId: ArticleId): Article
    fun create(article: Article)
    fun update(article: Article)
    fun delete(article: Article)
}