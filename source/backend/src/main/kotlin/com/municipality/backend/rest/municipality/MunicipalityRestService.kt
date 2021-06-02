package com.municipality.backend.rest.municipality

import com.municipality.backend.application.municipality.MunicipalityAppService
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.FIRST_PAGE
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.municipality.Municipality
import com.municipality.backend.rest.core.PageDto
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/municipality")
@Transactional
class MunicipalityRestService(
    private val municipalityAppService: MunicipalityAppService,
    private val loggedInUserResolver: LoggedInUserResolver
) {
    @GetMapping
    fun allMunicipalities(@RequestParam("page") page: Int?): ResponseEntity<PageDto<Municipality, MunicipalityDto>> {
        val all = municipalityAppService.all(
            loggedInUserResolver.loggedIn(),
            if (page == null) FIRST_PAGE else PageNumber(page),
            DEFAULT_PAGE_SIZE
        )
        val pageDto = PageDto(all) { municipality ->
            MunicipalityDto(
                municipality.id.rawId.toString(),
                municipality.name.name!!,
                municipality.nameInArabic.nameInArabic!!
            )
        }
        return ResponseEntity.ok(pageDto)
    }
}

data class MunicipalityDto(val id: String, val name: String, val nameInArabic: String)