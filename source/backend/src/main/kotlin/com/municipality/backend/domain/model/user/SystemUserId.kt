package com.municipality.backend.domain.model.user

import java.util.*

const val SYSTEM_ID_RAW = "_SYSTEM_ID_"
private val SYSTEM_ID = UUID.nameUUIDFromBytes(SYSTEM_ID_RAW.encodeToByteArray())

class SystemUserId : UserId(SYSTEM_ID) {
    companion object {
        val instance = SystemUserId()
    }
}

