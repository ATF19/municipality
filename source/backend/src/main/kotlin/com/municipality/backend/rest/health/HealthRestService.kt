package com.municipality.backend.rest.health

import com.municipality.backend.domain.service.core.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("health")
class HealthRestService(
        private val logger: Logger
) {

    @GetMapping
    fun check(): ResponseEntity<String> {
        logger.info(javaClass, "Health check: OK")
        return ResponseEntity.ok("I'm healthy :)")
    }
}