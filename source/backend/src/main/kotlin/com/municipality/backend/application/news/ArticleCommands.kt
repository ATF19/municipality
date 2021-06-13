package com.municipality.backend.application.news

import com.municipality.backend.application.Command
import com.municipality.backend.domain.model.news.ArticleId
import com.municipality.backend.domain.model.news.Content
import com.municipality.backend.domain.model.news.Title
import com.municipality.backend.domain.model.user.User

data class CreateArticleCommand(
    override val user: User<*>,
    val title: Title,
    val content: Content
) : Command()

data class UpdateArticleCommand(
    override val user: User<*>,
    val articleId: ArticleId,
    val title: Title,
    val content: Content
) : Command()