package com.municipality.backend.infrastructure.persistence.datasource

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class DataSourceProperties (
    @Value("\${municipality.datasource.host}")
    val host: String,

    @Value("\${municipality.datasource.port}")
    val port: String,

    @Value("\${municipality.datasource.database}")
    val database: String,

    @Value("\${municipality.datasource.username}")
    val username: String,

    @Value("\${municipality.datasource.password}")
    val password: String,

    @Value("\${municipality.datasource.schema}")
    val schema: String
)