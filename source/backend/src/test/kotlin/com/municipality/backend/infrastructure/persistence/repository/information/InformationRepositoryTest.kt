package com.municipality.backend.infrastructure.persistence.repository.information

import com.municipality.backend.domain.model.complaint.Phone
import com.municipality.backend.domain.model.information.Information
import com.municipality.backend.domain.model.information.InformationBuilder
import com.municipality.backend.domain.model.information.Intro
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.service.information.Informations
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test


class InformationRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var informations: Informations

    @Autowired
    private lateinit var informationJpaRepository: InformationJpaRepository

    @Test(groups = [TestGroup.INTEGRATION])
    fun save_new_information() {
        // given
        informationJpaRepository.deleteAll()
        val information = InformationBuilder().build()

        // when
        informations.save(information)

        // then
        Assertions.assertThat(informations.get()).isEqualTo(information)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun save_existing_information() {
        // given
        informationJpaRepository.deleteAll()
        val information = InformationBuilder().build()
        informations.save(information)
        val information2 = InformationBuilder().build()
        information2.intro = Intro("Hey there")
        information2.email = Email("atef@demo.com")
        information2.phone = Phone("12345678")

        // when
        informations.save(information2)

        // then
        Assertions.assertThat(informations.get()).isEqualTo(information2)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun get_information() {
        // given
        informationJpaRepository.deleteAll()
        val information = InformationBuilder().build()
        informations.save(information)

        // when
        val result = informations.get()

        // then
        Assertions.assertThat(result).isEqualTo(information)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun get_default_information() {
        // given
        informationJpaRepository.deleteAll()
        val default = Information()
        default.intro = Intro("")
        default.email = Email("")
        default.phone = Phone("")

        // when
        val result = informations.get()

        // then
        Assertions.assertThat(result).isEqualTo(default)
    }
}