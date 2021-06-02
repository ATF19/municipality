package com.municipality.backend.rest.district

import com.municipality.backend.application.district.DistrictAppService
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.FIRST_PAGE
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.district.District
import com.municipality.backend.rest.core.PageDto
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/district")
@Transactional
class DistrictRestService(
    private val districtAppService: DistrictAppService,
    private val loggedInUserResolver: LoggedInUserResolver
) {
    @GetMapping
    fun allDistricts(@RequestParam("page") page: Int?): ResponseEntity<PageDto<District, DistrictDto>> {
        val all = districtAppService.all(
            loggedInUserResolver.loggedIn(),
            if (page == null) FIRST_PAGE else PageNumber(page),
            DEFAULT_PAGE_SIZE
        )
        val pageDto = PageDto(all) { district ->
            DistrictDto(
                district.id.rawId.toString(),
                district.name.name!!,
                district.nameInArabic.nameInArabic!!,
                district.municipality.id.rawId.toString(),
                district.municipality.name.name!!,
                district.municipality.nameInArabic.nameInArabic!!
            )
        }
        return ResponseEntity.ok(pageDto)
    }
}

data class DistrictDto(val id: String, val name: String, val nameInArabic: String,
    val municipalityId: String, val municipalityName: String, val municipalityNameInArabic: String)