package com.municipality.backend.domain.model.user

interface User<T : UserId> {
    fun id() : T
}