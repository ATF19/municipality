package com.municipality.backend.domain.model.file

import com.municipality.backend.domain.model.core.Id
import javax.persistence.Embeddable

@Embeddable
class FileId : Id {
    constructor(): super()
    constructor(rawUuid: String): super(rawUuid)
}