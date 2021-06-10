package com.municipality.backend.infrastructure.persistence.repository.information

import com.municipality.backend.domain.model.complaint.Phone
import com.municipality.backend.domain.model.information.Information
import com.municipality.backend.domain.model.information.InformationId
import com.municipality.backend.domain.model.information.Intro
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.service.information.Informations
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class InformationRepository(
    private val informationJpaRepository: InformationJpaRepository
) : Informations {

    override fun save(information: Information) {
        informationJpaRepository.save(information)
    }

    override fun get(): Information =
        informationJpaRepository
            .findById(InformationId.DEFAULT)
            .orElseGet { getDefaultInformation() }

    private fun getDefaultInformation(): Information {
        val information = Information()
        information.intro = Intro("")
        information.email = Email("")
        information.phone = Phone("")
        return information
    }
}

interface InformationJpaRepository : JpaRepository<Information, InformationId>