package com.municipality.backend.infrastructure.persistence.migration

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FlywayRunner(
    @Value("\${municipality.migration.migrate}")
    val shouldMigrate: Boolean,
    private val flywayConfiguration: FlywayConfiguration
) : InitializingBean {

    override fun afterPropertiesSet() {
        if (shouldMigrate) {
            val flyway = flywayConfiguration.flyway()
            flyway.migrate()
        }
    }

}