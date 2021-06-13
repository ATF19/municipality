package com.municipality.backend.application.news

import com.municipality.backend.application.user.MissingInformationException
import com.municipality.backend.application.utility.HtmlSanitizer
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.news.Article
import com.municipality.backend.domain.model.news.ArticleId
import com.municipality.backend.domain.model.news.Content
import com.municipality.backend.domain.model.news.Title
import com.municipality.backend.domain.model.user.User
import com.municipality.backend.domain.service.news.Articles
import org.springframework.stereotype.Component

@Component
class ArticleAppService(
    private val articles: Articles
) {

    fun create(command: CreateArticleCommand) {
        if (!command.user.isAdmin())
            throw InsufficientPermissionException()

        verifyNoMissingInformation(command.title, command.content)
        val article = Article()
        article.title = command.title
        article.content = Content(HtmlSanitizer.sanitze(command.content.content!!))
        articles.create(article)
    }

    fun update(command: UpdateArticleCommand) {
        if (!command.user.isAdmin())
            throw InsufficientPermissionException()

        verifyNoMissingInformation(command.title, command.content)
        val article = articles.by(command.articleId)
        article.title = command.title
        article.content = Content(HtmlSanitizer.sanitze(command.content.content!!))
        articles.update(article)
    }

    fun delete(user: User<*>, articleId: ArticleId) {
        if (!user.isAdmin())
            throw InsufficientPermissionException()

        val articleToDelete = articles.by(articleId)
        articles.delete(articleToDelete)
    }

    fun by(articleId: ArticleId): Article = articles.by(articleId)

    fun all(pageNumber: PageNumber, pageSize: PageSize) = articles.all(pageNumber, pageSize)

    private fun verifyNoMissingInformation(title: Title, content: Content) {
        if (title.title.isNullOrEmpty() || content.content.isNullOrEmpty())
            throw MissingInformationException()
    }
}