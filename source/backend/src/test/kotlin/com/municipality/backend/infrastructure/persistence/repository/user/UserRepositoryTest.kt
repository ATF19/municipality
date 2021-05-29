package com.municipality.backend.infrastructure.persistence.repository.user

import com.municipality.backend.domain.model.core.DEFAULT_PAGE_SIZE
import com.municipality.backend.domain.model.core.FIRST_PAGE
import com.municipality.backend.domain.model.core.PageNumber
import com.municipality.backend.domain.model.municipality.MunicipalityId
import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.domain.model.user.RegisteredUserBuilder
import com.municipality.backend.domain.model.user.RegisteredUserId
import com.municipality.backend.domain.model.user.Username
import com.municipality.backend.domain.model.user.role.Admin
import com.municipality.backend.domain.model.user.role.MunicipalityAuditor
import com.municipality.backend.domain.model.user.role.Roles
import com.municipality.backend.domain.service.user.Users
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

class UserRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var repository: Users

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository


    @Test(groups = [TestGroup.INTEGRATION])
    fun register_user() {
        // given
        val builder = RegisteredUserBuilder()
        builder.roles = Roles.of(Admin(), MunicipalityAuditor(MunicipalityId()))
        val user = builder.build()

        // when
        repository.register(user)

        // then
        assertThat(repository.by(user.username)).contains(user)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun throw_exception_if_id_was_not_found() {
        // given

        // when

        // then
        assertThatThrownBy { repository.by(RegisteredUserId()) }
            .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_by_id() {
        // given
        val user = RegisteredUserBuilder().build()
        repository.register(user)

        // when
        val result = repository.by(user.id)

        // then
        assertThat(result).isEqualTo(user)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun return_empty_username_was_not_found() {
        // given

        // when
        val result = repository.by(Username("not found"))

        // then
        assertThat(result).isNotPresent
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_by_username() {
        // given
        val user = RegisteredUserBuilder().build()
        repository.register(user)

        // when
        val result = repository.by(user.username)

        // then
        assertThat(result).contains(user)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun username_does_not_exist() {
        // given

        // when
        val result = repository.exists(Username("not found"))

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun username_exists() {
        // given
        val user = RegisteredUserBuilder().build()
        repository.register(user)

        // when
        val result = repository.exists(user.username)

        // then
        assertThat(result).isTrue
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun email_does_not_exist() {
        // given

        // when
        val result = repository.exists(Email("notfound@demo.com"))

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun email_exists() {
        // given
        val user = RegisteredUserBuilder().build()
        repository.register(user)

        // when
        val result = repository.exists(user.email)

        // then
        assertThat(result).isTrue
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun find_all() {
        // given
        userJpaRepository.deleteAll()
        val user1 = RegisteredUserBuilder().build()
        val user2 = RegisteredUserBuilder().build()
        val user3 = RegisteredUserBuilder().build()
        val user4 = RegisteredUserBuilder().build()
        val user5 = RegisteredUserBuilder().build()
        val user6 = RegisteredUserBuilder().build()
        val user7 = RegisteredUserBuilder().build()
        val user8 = RegisteredUserBuilder().build()
        val user9 = RegisteredUserBuilder().build()
        val user10 = RegisteredUserBuilder().build()
        val user11 = RegisteredUserBuilder().build()
        val user12 = RegisteredUserBuilder().build()
        repository.register(user1)
        repository.register(user2)
        repository.register(user3)
        repository.register(user4)
        repository.register(user5)
        repository.register(user6)
        repository.register(user7)
        repository.register(user8)
        repository.register(user9)
        repository.register(user10)
        repository.register(user11)
        repository.register(user12)

        // when
        val firstPage = repository.all(FIRST_PAGE, DEFAULT_PAGE_SIZE)
        val secondPage = repository.all(PageNumber(2), DEFAULT_PAGE_SIZE)
        val thirdPage = repository.all(PageNumber(3), DEFAULT_PAGE_SIZE)

        // then
        assertThat(firstPage.elements).containsExactlyInAnyOrder(user12, user11, user10, user9, user8, user7,
        user6, user5, user4, user3)
        assertThat(firstPage.pageNumber.number).isEqualTo(1)
        assertThat(firstPage.totalPages).isEqualTo(2)
        assertThat(secondPage.elements).containsExactlyInAnyOrder(user2, user1)
        assertThat(secondPage.pageNumber.number).isEqualTo(2)
        assertThat(secondPage.totalPages).isEqualTo(2)
        assertThat(thirdPage.elements).isEmpty()
        assertThat(thirdPage.pageNumber.number).isEqualTo(3)
        assertThat(thirdPage.totalPages).isEqualTo(2)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun update_user() {
        // given
        val builder = RegisteredUserBuilder()
        builder.roles = Roles.of(Admin(), MunicipalityAuditor(MunicipalityId()))
        val user = builder.build()
        repository.register(user)
        val newEmail = Email("newEmail@test.com")
        user.email = newEmail

        // when
        repository.update(user)

        // then
        val by = repository.by(user.id)
        assertThat(by.email).isEqualTo(newEmail)
        assertThat(by).isEqualTo(user)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun delete_user() {
        // given
        val builder = RegisteredUserBuilder()
        builder.roles = Roles.of(Admin(), MunicipalityAuditor(MunicipalityId()))
        val user = builder.build()
        repository.register(user)

        // when
        repository.delete(user)

        // then
        assertThatThrownBy { repository.by(user.id) }
            .isInstanceOf(NoSuchElementException::class.java)
    }
}