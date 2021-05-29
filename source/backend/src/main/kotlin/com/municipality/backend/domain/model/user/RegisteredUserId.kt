package com.municipality.backend.domain.model.user

import java.util.*
import javax.persistence.Embeddable

@Embeddable
class RegisteredUserId : UserId {
    constructor(): super()
    constructor(uuid: UUID): super(uuid)
    constructor(rawUuid: String): super(rawUuid)
}