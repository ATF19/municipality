package com.municipality.backend.infrastructure.springboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.municipality.backend"])
class MunicipalityBackendApplication

fun main(args: Array<String>) {
    runApplication<MunicipalityBackendApplication>(*args)
}
