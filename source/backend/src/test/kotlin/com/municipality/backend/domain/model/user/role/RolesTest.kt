package com.municipality.backend.domain.model.user.role

import com.municipality.backend.domain.model.district.DistrictId
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class RolesTest {

    @Test(groups = [TestGroup.UNIT])
    fun create_empty_roles() {
        // given

        // when
        val roles = Roles.empty()

        // then
        assertThat(roles.all()).isEmpty()
    }

    @Test(groups = [TestGroup.UNIT])
    fun create_from_roles() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())

        // when
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)

        // then
        assertThat(roles.all()).containsExactlyInAnyOrder(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)
    }

    @Test(groups = [TestGroup.UNIT])
    fun grant_role() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)
        val mAuditor3 = MunicipalityAuditor(MunicipalityId())

        // when
        roles.grant(mAuditor3)

        // then
        assertThat(roles.all())
            .containsExactlyInAnyOrder(admin, mAuditor1, mAuditor2, mResponsible, dResponsible, mAuditor3)
    }

    @Test(groups = [TestGroup.UNIT])
    fun revoke_role() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)

        // when
        roles.revoke(admin)

        // then
        assertThat(roles.all()).containsExactlyInAnyOrder(mAuditor1, mAuditor2, mResponsible, dResponsible)
    }

    @Test(groups = [TestGroup.UNIT])
    fun is_admin() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)

        // when
        val result = roles.isAdmin()

        // then
        assertThat(result).isTrue
    }

    @Test(groups = [TestGroup.UNIT])
    fun is_municipality_responsible() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)

        // when
        val result = roles.isMunicipalityResponsible(mResponsible.municipalityId)

        // then
        assertThat(result).isTrue
    }

    @Test(groups = [TestGroup.UNIT])
    fun is_municipality_responsible_on_different_municipality() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)

        // when
        val result = roles.isMunicipalityResponsible(MunicipalityId())

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun is_municipality_auditor() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)

        // when
        val result = roles.isMunicipalityAuditor(mAuditor1.municipalityId)

        // then
        assertThat(result).isTrue
    }

    @Test(groups = [TestGroup.UNIT])
    fun is_municipality_auditor_on_different_municipality() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)

        // when
        val result = roles.isMunicipalityAuditor(mResponsible.municipalityId)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun is_district_responsible() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)

        // when
        val result = roles.isDistrictResponsible(dResponsible.districtId)

        // then
        assertThat(result).isTrue
    }

    @Test(groups = [TestGroup.UNIT])
    fun is_district_responsible_on_different_district() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible)

        // when
        val result = roles.isDistrictResponsible(DistrictId())

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun is_district_auditor() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val dAuditor = DistrictAuditor(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible, dAuditor)

        // when
        val result = roles.isDistrictAuditor(dAuditor.districtId)

        // then
        assertThat(result).isTrue
    }

    @Test(groups = [TestGroup.UNIT])
    fun is_district_auditor_on_different_district() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val dAuditor = DistrictAuditor(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible, dAuditor)

        // when
        val result = roles.isDistrictAuditor(DistrictId())

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_municipalities_ids() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val dAuditor = DistrictAuditor(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible, dAuditor)

        // when
        val result = roles.municipalities()

        // then
        assertThat(result).containsExactlyInAnyOrder(mResponsible.municipalityId, mAuditor1.municipalityId,
            mAuditor2.municipalityId)
    }

    @Test(groups = [TestGroup.UNIT])
    fun get_districts_ids() {
        // given
        val admin = Admin()
        val mResponsible = MunicipalityResponsible(MunicipalityId())
        val mAuditor1 = MunicipalityAuditor(MunicipalityId())
        val mAuditor2 = MunicipalityAuditor(MunicipalityId())
        val dResponsible = DistrictResponsible(DistrictId())
        val dAuditor = DistrictAuditor(DistrictId())
        val roles = Roles.of(admin, mAuditor1, mAuditor2, mResponsible, dResponsible, dAuditor)

        // when
        val result = roles.districts()

        // then
        assertThat(result).containsExactlyInAnyOrder(dResponsible.districtId, dAuditor.districtId)
    }
}