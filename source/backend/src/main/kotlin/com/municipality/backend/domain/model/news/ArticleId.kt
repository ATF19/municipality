package com.municipality.backend.domain.model.news

import com.municipality.backend.domain.model.core.Id
import javax.persistence.Embeddable

@Embeddable
class ArticleId : Id {
    constructor(): super()
    constructor(rawUuid: String): super(rawUuid)
}