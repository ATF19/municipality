package com.municipality.backend.domain.model.municipality

class MunicipalityBuilder {
    var id = MunicipalityId()
    var name = MunicipalityName("Tunis")
    var nameInArabic = MunicipalityNameInArabic("تونس")

    fun build(): Municipality {
        val municipality = Municipality(id)
        municipality.name = name
        municipality.nameInArabic = nameInArabic
        return municipality
    }
}