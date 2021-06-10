package com.municipality.backend.domain.model.information

import com.municipality.backend.domain.model.core.Id
import java.util.*
import javax.persistence.Embeddable

private const val DEFAULT_INFO_ID = "__DEFAULT_INFO_ID__"

@Embeddable
class InformationId : Id {

    companion object {
        val DEFAULT = InformationId(UUID.nameUUIDFromBytes(DEFAULT_INFO_ID.toByteArray()))
    }

    constructor(): super()
    constructor(uuid: UUID): super(uuid)
    constructor(rawUuid: String): super(rawUuid)
}