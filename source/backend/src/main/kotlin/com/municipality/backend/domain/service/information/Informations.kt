package com.municipality.backend.domain.service.information

import com.municipality.backend.domain.model.information.Information

interface Informations {
    fun save(information: Information)
    fun get(): Information
}