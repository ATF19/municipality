package com.municipality.backend.infrastructure.persistence.audit

import org.hibernate.envers.AuditReader
import org.hibernate.envers.AuditReaderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import javax.persistence.EntityManagerFactory

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", modifyOnCreate = false)
class AuditConfiguration(
    private val entityManagerFactory: EntityManagerFactory
) {
    @Bean
    fun auditReader(): AuditReader = AuditReaderFactory.get(entityManagerFactory.createEntityManager())
}