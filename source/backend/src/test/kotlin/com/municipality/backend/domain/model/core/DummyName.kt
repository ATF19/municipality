package com.municipality.backend.domain.model.core

import javax.persistence.Embeddable

@Embeddable
data class DummyName(val name: String? = null)