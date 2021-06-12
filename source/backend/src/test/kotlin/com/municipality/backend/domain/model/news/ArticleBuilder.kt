package com.municipality.backend.domain.model.news

class ArticleBuilder {

    var id = ArticleId()
    var title = Title("Dummy Article Title")
    var content = Content("<p>This is a dummy article.</p>")

    fun build(): Article {
        val article = Article(id)
        article.title = title
        article.content = content
        return article
    }

    fun buildWithoutContent() = ArticleWithoutContent(id, title)
}