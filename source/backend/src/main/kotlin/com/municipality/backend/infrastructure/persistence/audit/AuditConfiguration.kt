package com.municipality.backend.infrastructure.persistence.audit

import com.municipality.backend.domain.model.user.AnonymousUser
import org.hibernate.envers.AuditReader
import org.hibernate.envers.AuditReaderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import javax.persistence.EntityManagerFactory

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", modifyOnCreate = false)
class AuditConfiguration(
    private val entityManagerFactory: EntityManagerFactory
) {
    @Bean
    fun auditReader(): AuditReader = AuditReaderFactory.get(entityManagerFactory.createEntityManager())

    @Bean
    fun auditorAware(): AuditorAware<String> = HttpSessionAuditAware()

    private class HttpSessionAuditAware : AuditorAware<String> {
        override fun getCurrentAuditor(): Optional<String> = Optional.of(name())

        private fun name(): String {
            if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().authentication == null)
                return AnonymousUser().name()
            return SecurityContextHolder.getContext().authentication.name
        }
    }
}