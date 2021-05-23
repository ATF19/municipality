package com.municipality.backend.infrastructure.springboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.municipality.backend"])
class MunicipalityBackendApplication

fun main(args: Array<String>) {
	runApplication<MunicipalityBackendApplication>(*args)
}
