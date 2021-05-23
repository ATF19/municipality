package com.municipality.backend.infrastructure.persistence.migration

import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class FlywayRunner(
    private val flywayConfiguration: FlywayConfiguration
) : InitializingBean {

    override fun afterPropertiesSet() {
        val flyway = flywayConfiguration.flyway()
        flyway.migrate()
    }

}