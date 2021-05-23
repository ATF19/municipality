package com.municipality.backend.domain.model.user

class SystemUser : User<SystemUserId> {
    override fun id() = SystemUserId.instance
}