package com.municipality.backend.infrastructure.persistence.repository

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories("com.municipality.backend.infrastructure")
@EntityScan("com.municipality.backend")
class RepositoryConfiguration