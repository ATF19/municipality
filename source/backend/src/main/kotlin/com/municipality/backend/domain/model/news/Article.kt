package com.municipality.backend.domain.model.news

import com.municipality.backend.domain.model.core.DomainEntity
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity

@Entity
class Article : DomainEntity<ArticleId> {

    lateinit var title: Title

    @Column(name = "content", columnDefinition = "TEXT")
    lateinit var content: Content

    constructor() : super(ArticleId())
    constructor(id: ArticleId) : super(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Article

        if (title != other.title) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        return result
    }

    override fun toString(): String {
        return "Article(title=$title, content=$content)"
    }
}

@Embeddable
data class Title(val title: String? = null)

@Embeddable
data class Content(val content: String? = null)