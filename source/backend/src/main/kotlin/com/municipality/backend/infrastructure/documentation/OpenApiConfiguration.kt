package com.municipality.backend.infrastructure.documentation

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration(
    @Value("\${municipality.backend.version}")
    private val version: String
) {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .components(Components())
        .info(Info().title("Municipality API").description("").version(version))
}