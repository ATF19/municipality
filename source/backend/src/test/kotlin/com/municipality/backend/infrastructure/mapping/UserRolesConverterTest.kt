package com.municipality.backend.infrastructure.mapping

import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.user.role.*
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class UserRolesConverterTest {

    @Test(groups = [TestGroup.UNIT])
    fun serialize_null_roles() {
        // given
        val userRolesConverter = UserRolesConverter()

        // when
        val result = userRolesConverter.convertToDatabaseColumn(null)

        // then
        assertThat(result).isEmpty()
    }

    @Test(groups = [TestGroup.UNIT])
    fun serialize_empty_roles() {
        // given
        val userRolesConverter = UserRolesConverter()

        // when
        val result = userRolesConverter.convertToDatabaseColumn(Roles.empty())

        // then
        assertThat(result).isEmpty()
    }

    @Test(groups = [TestGroup.UNIT])
    fun serialize_roles() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val dAuditor = DistrictAuditor(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible, dAuditor)
        val userRolesConverter = UserRolesConverter()

        // when
        val result = userRolesConverter.convertToDatabaseColumn(roles)

        // then
        val serializedRoles = result.split(";")
        assertThat(serializedRoles).containsExactlyInAnyOrder("ADMIN", "MUNICIPALITY_RESPONSIBLE|${mResponsible.municipalityId.rawId}",
            "MUNICIPALITY_AUDITOR|${mAuditor1.municipalityId.rawId}", "MUNICIPALITY_AUDITOR|${mAuditor2.municipalityId.rawId}",
            "DISTRICT_RESPONSIBLE|${dResponsible.districtId.rawId}", "DISTRICT_AUDITOR|${dAuditor.districtId.rawId}")
    }

    @Test(groups = [TestGroup.UNIT])
    fun deserialize_null_roles() {
        // given
        val userRolesConverter = UserRolesConverter()

        // when
        val result = userRolesConverter.convertToEntityAttribute(null)

        // then
        assertThat(result.all()).isEmpty()
    }

    @Test(groups = [TestGroup.UNIT])
    fun deserialize_empty_roles() {
        // given
        val userRolesConverter = UserRolesConverter()

        // when
        val result = userRolesConverter.convertToEntityAttribute("")

        // then
        assertThat(result.all()).isEmpty()
    }

    @Test(groups = [TestGroup.UNIT])
    fun deserialize_roles() {
        // given
        val municipalityId1 = MunicipalityId()
        val municipalityId2 = MunicipalityId()
        val municipalityId3 = MunicipalityId()
        val districtId1 = DistrictId()
        val districtId2 = DistrictId()
        val districtId3 = DistrictId()
        val serialzed = "DISTRICT_AUDITOR|${districtId1.rawId};DISTRICT_AUDITOR|${districtId2.rawId};DISTRICT_RESPONSIBLE|${districtId3.rawId};" +
                "MUNICIPALITY_RESPONSIBLE|${municipalityId1.rawId};ADMIN;MUNICIPALITY_RESPONSIBLE|${municipalityId2.rawId};MUNICIPALITY_AUDITOR|${municipalityId3.rawId}"
        val userRolesConverter = UserRolesConverter()

        // when
        val result = userRolesConverter.convertToEntityAttribute(serialzed)

        // then
        assertThat(result.all()).containsExactlyInAnyOrder(
            Admin(), MunicipalityResponsible(municipalityId1), MunicipalityResponsible(municipalityId2), DistrictResponsible(districtId3),
            DistrictAuditor(districtId1), DistrictAuditor(districtId2), MunicipalityAuditor(municipalityId3))
    }
}