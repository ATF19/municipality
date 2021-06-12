package com.municipality.backend.application.news

import com.municipality.backend.application.user.MissingInformationException
import com.municipality.backend.domain.model.core.Page
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.core.error.InsufficientPermissionException
import com.municipality.backend.domain.model.news.*
import com.municipality.backend.domain.model.user.RegisteredUserBuilder
import com.municipality.backend.domain.service.news.Articles
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.testng.annotations.Test

class ArticleAppServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_when_non_admin_tries_to_create_an_article() {
        // given
        val user = RegisteredUserBuilder().build()
        val articles = mockk<Articles>()
        val appService = ArticleAppService(articles)
        val title = Title("Test title")
        val content = Content("Test content")
        val command = CreateArticleCommand(user, title, content)

        // when

        // then
        assertThatThrownBy { appService.create(command) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_when_an_article_have_missing_info() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val articles = mockk<Articles>()
        val appService = ArticleAppService(articles)
        val title = Title("Test title")
        val content = Content("")
        val command = CreateArticleCommand(user, title, content)

        // when

        // then
        assertThatThrownBy { appService.create(command) }
            .isInstanceOf(MissingInformationException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun create_an_article() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val articles = mockk<Articles>(relaxed = true)
        val appService = ArticleAppService(articles)
        val title = Title("Test title")
        val content = Content("Test content")
        val command = CreateArticleCommand(user, title, content)

        // when
        appService.create(command)

        // then
        val captor = slot<Article>()
        verify { articles.create(capture(captor)) }
        val result = captor.captured
        assertThat(result.title).isEqualTo(title)
        assertThat(result.content).isEqualTo(content)
    }

    @Test(groups = [TestGroup.UNIT])
    fun throw_exception_when_non_admin_tries_to_delete_an_article() {
        // given
        val user = RegisteredUserBuilder().build()
        val articles = mockk<Articles>()
        val appService = ArticleAppService(articles)
        val articleId = ArticleId()

        // when

        // then
        assertThatThrownBy { appService.delete(user, articleId) }
            .isInstanceOf(InsufficientPermissionException::class.java)
    }

    @Test(groups = [TestGroup.UNIT])
    fun delete_an_article() {
        // given
        val user = RegisteredUserBuilder().admin().build()
        val articles = mockk<Articles>(relaxed = true)
        val appService = ArticleAppService(articles)
        val article = ArticleBuilder().build()
        every { articles.by(article.id) }.returns(article)

        // when
        appService.delete(user, article.id)

        // then
        val captor = slot<Article>()
        verify { articles.delete(capture(captor)) }
        assertThat(captor.captured).isEqualTo(article)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_by_id() {
        // given
        val articles = mockk<Articles>()
        val appService = ArticleAppService(articles)
        val article = ArticleBuilder().build()
        every { articles.by(article.id) }.returns(article)

        // when
        val result = appService.by(article.id)

        // then
        assertThat(result).isEqualTo(article)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_all() {
        // given
        val articles = mockk<Articles>()
        val appService = ArticleAppService(articles)
        val article1 = ArticleBuilder().build()
        val article2 = ArticleBuilder().build()
        val article3 = ArticleBuilder().build()
        val pageNumber = PageNumber(2)
        val pageSize = PageSize(3)
        val page = Page(listOf(articleWithoutTitle(article1), articleWithoutTitle(article2), articleWithoutTitle(article3)), pageNumber, pageSize, 1)
        every { articles.all(pageNumber, pageSize) }.returns(page)

        // when
        val result = appService.all(pageNumber, pageSize)

        // then
        assertThat(result).isEqualTo(page)
    }

    private fun articleWithoutTitle(article: Article) = ArticleWithoutContent(article.id, article.title)
}