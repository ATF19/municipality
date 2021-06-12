package com.municipality.backend.domain.model.municipality

import com.municipality.backend.domain.model.core.Id
import javax.persistence.Embeddable

@Embeddable
class MunicipalityId : Id {
    constructor() : super()
    constructor(rawUuid: String) : super(rawUuid)
}