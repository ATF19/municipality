package com.municipality.backend.infrastructure.persistence.datasource

import org.postgresql.Driver
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource
import kotlin.reflect.jvm.jvmName

@Configuration
@EnableTransactionManagement
class DataSourceProvider(
    private val dataSourceProperties: DataSourceProperties
) {
    @Bean
    fun dataSource(): DataSource {
        val dbUrl = "jdbc:postgresql://${dataSourceProperties.host}" +
                ":${dataSourceProperties.port}/${dataSourceProperties.database}"
        return DataSourceBuilder
            .create()
            .driverClassName(Driver::class.jvmName)
            .url(dbUrl)
            .username(dataSourceProperties.username)
            .password(dataSourceProperties.password)
            .build()
    }
}