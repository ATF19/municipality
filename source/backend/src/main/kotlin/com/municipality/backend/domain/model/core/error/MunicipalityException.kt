package com.municipality.backend.domain.model.core.error

open class MunicipalityException : RuntimeException {
    constructor(): super()
    constructor(message: String): super(message)
    constructor(throwable: Throwable): super(throwable)
    constructor(message: String, throwable: Throwable): super(message, throwable)
}

open class DomainException : MunicipalityException {
    constructor(): super()
    constructor(message: String): super(message)
    constructor(throwable: Throwable): super(throwable)
    constructor(message: String, throwable: Throwable): super(message, throwable)
}

class InsufficientPermissionException : MunicipalityException()

