package com.municipality.backend.infrastructure.persistence.repository.news

import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.news.Article
import com.municipality.backend.domain.model.news.ArticleId
import com.municipality.backend.domain.model.news.ArticleWithoutContent
import com.municipality.backend.domain.service.news.Articles
import com.municipality.backend.infrastructure.persistence.repository.PageBuilder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class ArticleRepository(
    private val articleJpaRepository: ArticleJpaRepository
) : Articles {

    override fun all(pageNumber: PageNumber, pageSize: PageSize): Page<ArticleWithoutContent> {
        val all = articleJpaRepository
            .findAll(PageBuilder.builder.build(pageNumber, pageSize))
            .map { ArticleWithoutContent(it.id, it.title, it.createdAt, it.createdBy) }
        return Page(all.content, pageNumber, pageSize, all.totalPages)
    }

    override fun by(articleId: ArticleId): Article = articleJpaRepository
        .findById(articleId)
        .orElseThrow { NoSuchElementException("No article exists with the ID '${articleId.rawId}'") }

    override fun create(article: Article) {
        articleJpaRepository.save(article)
    }

    override fun update(article: Article) {
        articleJpaRepository.save(article)
    }

    override fun delete(article: Article) {
        articleJpaRepository.delete(article)
    }
}

interface ArticleJpaRepository : JpaRepository<Article, ArticleId>