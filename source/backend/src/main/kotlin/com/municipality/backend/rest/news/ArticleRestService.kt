package com.municipality.backend.rest.news

import com.municipality.backend.application.news.ArticleAppService
import com.municipality.backend.application.news.CreateArticleCommand
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.FIRST_PAGE
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.news.ArticleId
import com.municipality.backend.domain.model.news.ArticleWithoutContent
import com.municipality.backend.domain.model.news.Content
import com.municipality.backend.domain.model.news.Title
import com.municipality.backend.rest.core.PageDto
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/articles")
@Transactional
class ArticleRestService(
    private val articleAppService: ArticleAppService,
    private val loggedInUserResolver: LoggedInUserResolver
) {
    @GetMapping
    fun allArticles(@RequestParam("page") page: Int?): ResponseEntity<PageDto<ArticleWithoutContent, ArticleWithoutContentDto>> {
        val all = articleAppService.all(
            if (page == null) FIRST_PAGE else PageNumber(page),
            DEFAULT_PAGE_SIZE
        )
        val pageDto = PageDto(all) { ArticleWithoutContentDto(it.id.rawId.toString(), it.title.title!!) }
        return ResponseEntity.ok(pageDto)
    }

    @PostMapping
    fun createArticle(@RequestBody request: CreateArticleRequest): ResponseEntity<String> {
        val command = CreateArticleCommand(loggedInUserResolver.loggedIn(),
            Title(request.title), Content(request.content))
        articleAppService.create(command)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{articleId}")
    fun deleteArticle(@PathVariable("articleId") articleId: String): ResponseEntity<String> {
        articleAppService.delete(loggedInUserResolver.loggedIn(), ArticleId(articleId))
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{articleId}")
    fun getArticle(@PathVariable("articleId") articleId: String): ResponseEntity<ArticleDto> {
        val article = articleAppService.by(ArticleId(articleId))
        return ResponseEntity.ok(ArticleDto(article.id.rawId.toString(), article.title.title!!, article.content.content!!))
    }
}

data class ArticleWithoutContentDto(val id: String, val title: String)
data class ArticleDto(val id: String, val title: String, val content: String)
data class CreateArticleRequest(val title: String, val content: String)