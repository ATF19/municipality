package com.municipality.backend.infrastructure.persistence.repository.news

import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.core.PageSize
import com.municipality.backend.domain.model.news.*
import com.municipality.backend.domain.service.news.Articles
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

class ArticleRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    lateinit var articleJpaRepository: ArticleJpaRepository

    @Autowired
    lateinit var articles: Articles

    @Test(groups = [TestGroup.INTEGRATION])
    fun create_article() {
        // given
        val article = ArticleBuilder().build()

        // when
        articles.create(article)

        // then
        val byId = articleJpaRepository.getById(article.id)
        assertThat(byId.title).isEqualTo(article.title)
        assertThat(byId.content).isEqualTo(article.content)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun update_article() {
        // given
        val article = ArticleBuilder().build()
        articles.create(article)
        val articleToUpdate = articleJpaRepository.getById(article.id)
        articleToUpdate.title = Title("New title")

        // when
        articles.update(articleToUpdate)

        // then
        val byId = articleJpaRepository.getById(article.id)
        assertThat(byId.title).isEqualTo(articleToUpdate.title)
        assertThat(byId.content).isEqualTo(article.content)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun throw_exception_if_article_is_not_found() {
        // given

        // when

        // then
        assertThatThrownBy { articles.by(ArticleId()) }
            .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun retrieve_article_by_id() {
        // given
        val article = ArticleBuilder().build()
        articles.create(article)

        // when
        val byId = articles.by(article.id)

        // then
        assertThat(byId.title).isEqualTo(article.title)
        assertThat(byId.content).isEqualTo(article.content)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun delete_article() {
        // given
        val article = ArticleBuilder().build()
        articles.create(article)

        // when
        articles.delete(article)

        // then
        assertThatThrownBy { articles.by(article.id) }
            .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun retrieve_articles() {
        // given
        articleJpaRepository.deleteAll()
        val article1 = ArticleBuilder().build()
        val article2 = ArticleBuilder().build()
        val article3 = ArticleBuilder().build()
        val article4 = ArticleBuilder().build()
        val article5 = ArticleBuilder().build()
        articles.create(article1)
        articles.create(article2)
        articles.create(article3)
        articles.create(article4)
        articles.create(article5)
        val pageNumber = PageNumber(1)
        val pageSize = PageSize(4)

        // when
        val ids = articles.all(pageNumber, pageSize)

        // then
        assertThat(ids.pageNumber.number).isEqualTo(1)
        assertThat(ids.totalPages).isEqualTo(2)
        assertThat(ids.elements).containsExactlyInAnyOrder(
            articleWithoutTitle(articles.by(article5.id)),
            articleWithoutTitle(articles.by(article4.id)),
            articleWithoutTitle(articles.by(article3.id)),
            articleWithoutTitle(articles.by(article2.id))
        )
    }

    private fun articleWithoutTitle(article: Article) = ArticleWithoutContent(article.id, article.title, article.createdAt, article.createdBy)
}