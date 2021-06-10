package com.municipality.backend.domain.model.information

import com.municipality.backend.domain.model.complaint.Phone
import com.municipality.backend.domain.model.user.Email

class InformationBuilder {
    var intro = Intro("Just a demo intro")
    var phone = Phone("21612345")
    var email = Email("test@gmail.com")

    fun build(): Information {
        val information = Information()
        information.intro = intro
        information.phone = phone
        information.email = email
        return information
    }
}