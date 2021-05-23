package com.municipality.backend.infrastructure.persistence.migration

import com.municipality.backend.infrastructure.persistence.datasource.DataSourceProperties
import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfiguration(
    private val dataSource: DataSource,
    private val dataSourceProperties: DataSourceProperties
) {
    @Bean
    fun flyway(): Flyway {
        return Flyway
            .configure()
            .baselineOnMigrate(true)
            .dataSource(dataSource)
            .schemas(dataSourceProperties.schema)
            .defaultSchema(dataSourceProperties.schema)
            .createSchemas(true)
            .load()
    }
}