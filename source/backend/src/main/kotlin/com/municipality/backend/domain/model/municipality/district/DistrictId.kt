package com.municipality.backend.domain.model.municipality.district

import com.municipality.backend.domain.model.core.Id
import javax.persistence.Embeddable

@Embeddable
class DistrictId : Id {
    constructor(): super()
    constructor(rawUuid: String): super(rawUuid)
}