package com.municipality.backend.rest.news

import com.municipality.backend.application.news.ArticleAppService
import com.municipality.backend.application.news.CreateArticleCommand
import com.municipality.backend.application.news.UpdateArticleCommand
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.news.ArticleBuilder
import com.municipality.backend.domain.model.news.ArticleId
import com.municipality.backend.rest.core.PageDto
import com.municipality.backend.shared_code_for_tests.LoggedInUserForTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.testng.annotations.Test


class ArticleRestServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun get_all() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        val appService = mockk<ArticleAppService>()
        val restService = ArticleRestService(appService, loggedInUserResolver)
        val article1 = ArticleBuilder().buildWithoutContent()
        val article2 = ArticleBuilder().buildWithoutContent()
        val page = Page(listOf(article1, article2), PageNumber(1), DEFAULT_PAGE_SIZE, 1)
        every { appService.all(PageNumber(1), DEFAULT_PAGE_SIZE) }.returns(page)

        // when
        val response = restService.allArticles(1)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(PageDto(page) {
            ArticleWithoutContentDto(
                it.id.rawId.toString(),
                it.title.title!!
            )
        })
    }

    @Test(groups = [TestGroup.UNIT])
    fun create_article() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ArticleAppService>(relaxed = true)
        val restService = ArticleRestService(appService, loggedInUserResolver)
        val request = CreateOrUpdateArticleRequest("Test title", "Test content")

        // when
        val response = restService.createArticle(request)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val captor = slot<CreateArticleCommand>()
        verify { appService.create(capture(captor)) }
        val command = captor.captured
        assertThat(command.user).isEqualTo(loggedInUserResolver.loggedIn())
        assertThat(command.title.title).isEqualTo(request.title)
        assertThat(command.content.content).isEqualTo(request.content)
    }

    @Test(groups = [TestGroup.UNIT])
    fun update_article() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ArticleAppService>(relaxed = true)
        val restService = ArticleRestService(appService, loggedInUserResolver)
        val id = ArticleId()
        val request = CreateOrUpdateArticleRequest("Test title", "Test content")

        // when
        val response = restService.updateArticle(id.rawId.toString(), request)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val captor = slot<UpdateArticleCommand>()
        verify { appService.update(capture(captor)) }
        val command = captor.captured
        assertThat(command.user).isEqualTo(loggedInUserResolver.loggedIn())
        assertThat(command.articleId).isEqualTo(id)
        assertThat(command.title.title).isEqualTo(request.title)
        assertThat(command.content.content).isEqualTo(request.content)
    }

    @Test(groups = [TestGroup.UNIT])
    fun delete_article() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ArticleAppService>(relaxed = true)
        val restService = ArticleRestService(appService, loggedInUserResolver)
        val articleId = ArticleId()

        // when
        val response = restService.deleteArticle(articleId.rawId.toString())

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        verify { appService.delete(LoggedInUserForTest.user, eq(articleId)) }
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_article() {
        // given
        val loggedInUserResolver = mockk<LoggedInUserResolver>()
        every { loggedInUserResolver.loggedIn() }.returns(LoggedInUserForTest.user)
        val appService = mockk<ArticleAppService>(relaxed = true)
        val article = ArticleBuilder().build()
        val restService = ArticleRestService(appService, loggedInUserResolver)
        every { appService.by(article.id) }.returns(article)

        // when
        val response = restService.getArticle(article.id.rawId.toString())

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.id).isEqualTo(article.id.rawId.toString())
        assertThat(response.body!!.title).isEqualTo(article.title.title)
        assertThat(response.body!!.content).isEqualTo(article.content.content)
    }
}