package com.municipality.backend.rest.information

import com.municipality.backend.application.information.InformationAppService
import com.municipality.backend.application.information.SaveInformationCommand
import com.municipality.backend.application.user.LoggedInUserResolver
import com.municipality.backend.domain.model.complaint.Phone
import com.municipality.backend.domain.model.information.Intro
import com.municipality.backend.domain.model.user.Email
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/information")
@Transactional
class InformationRestService(
    private val informationAppService: InformationAppService,
    private val loggedInUserResolver: LoggedInUserResolver
) {

    @GetMapping
    fun getInformation(): ResponseEntity<InformationDto> {
        val information = informationAppService.get()
        return ResponseEntity.ok(InformationDto(information.intro.intro.orEmpty(), information.phone.phoneNumber.orEmpty(),
            information.email.email.orEmpty()))
    }

    @PostMapping
    fun saveInformation(@RequestBody informationDto: InformationDto): ResponseEntity<String> {
        val command = SaveInformationCommand(loggedInUserResolver.loggedIn(), Intro(informationDto.intro),
            Phone(informationDto.phone), Email(informationDto.email))
        informationAppService.save(command)
        return ResponseEntity.ok("")
    }
}

data class InformationDto(val intro: String, val phone: String, val email: String)