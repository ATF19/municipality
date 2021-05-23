package com.municipality.backend.domain.model.user

import com.municipality.backend.domain.model.core.Id
import java.util.*
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class UserId : Id {
    constructor(): super()
    constructor(uuid: UUID): super(uuid)

    fun isSystemId() = rawId == SystemUserId.instance.rawId
    fun isAnonymousId() = rawId == AnonymousUserId.instance.rawId
    fun isRegisteredUserId() = !isSystemId() && !isAnonymousId()
}