package com.municipality.backend.domain.model.complaint

import com.municipality.backend.domain.model.core.Id
import javax.persistence.Embeddable

@Embeddable
class ComplaintId : Id {
    constructor(): super()
    constructor(rawUuid: String): super(rawUuid)
}