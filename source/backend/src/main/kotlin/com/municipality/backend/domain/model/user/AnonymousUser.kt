package com.municipality.backend.domain.model.user

class AnonymousUser : User<AnonymousUserId> {
    override fun id() = AnonymousUserId.instance
}