package com.municipality.backend.domain.model.core

import javax.persistence.Embeddable

@Embeddable
data class DummyNumber(val number: Int? = null)
