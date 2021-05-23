package com.municipality.backend.domain.model.user

import java.util.*

const val ANONYMOUS_ID_RAW = "_ANONYMOUS_ID_"
private val ANONYMOUS_ID = UUID.nameUUIDFromBytes(ANONYMOUS_ID_RAW.encodeToByteArray())

class AnonymousUserId : UserId(ANONYMOUS_ID) {
    companion object {
        val instance = AnonymousUserId()
    }
}

