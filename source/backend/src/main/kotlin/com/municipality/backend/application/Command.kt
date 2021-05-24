package com.municipality.backend.application

import com.municipality.backend.domain.model.user.User

abstract class Command {
    abstract val user: User<*>
}
