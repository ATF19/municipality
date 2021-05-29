package com.municipality.backend.domain.model.district

import com.municipality.backend.domain.model.municipality.MunicipalityBuilder

class DistrictBuilder {
    var id = DistrictId()
    var name = DistrictName("Beb Souikda")
    var nameInArabic = DistrictNameInArabic("باب سويقة")
    var municipality = MunicipalityBuilder().build()

    fun build(): District {
        val district = District(id)
        district.name = name
        district.nameInArabic = nameInArabic
        district.municipality = municipality
        return district
    }
}