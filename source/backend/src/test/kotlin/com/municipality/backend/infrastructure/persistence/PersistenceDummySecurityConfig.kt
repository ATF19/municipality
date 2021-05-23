package com.municipality.backend.infrastructure.persistence

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import java.util.*

@Configuration
class PersistenceDummySecurityConfig {

    @Bean
    fun auditorAware(): AuditorAware<String> = DummyAuditorAware()

    private class DummyAuditorAware : AuditorAware<String> {
        override fun getCurrentAuditor(): Optional<String> = Optional.of("DUMMY_USER")
    }
}