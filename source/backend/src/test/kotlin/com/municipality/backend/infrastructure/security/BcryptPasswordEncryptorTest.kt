package com.municipality.backend.infrastructure.security

import com.municipality.backend.application.user.Passwords
import com.municipality.backend.application.user.UnencryptedPassword
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

class BcryptPasswordEncryptorTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var passwords: Passwords

    @Test(groups = [TestGroup.INTEGRATION])
    fun encrypt_password() {
        // given
        val unencryptedPassword = UnencryptedPassword("myRawPass")

        // when
        val encrypted = passwords.encrypt(unencryptedPassword)

        // then
        assertThat(encrypted.password).isNotEqualTo(unencryptedPassword.password)
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun passwords_do_not_match() {
        // given
        val unencryptedPassword1 = UnencryptedPassword("myRawPass")
        val unencryptedPassword2 = UnencryptedPassword("myOtherRawPass")
        val encrypted = passwords.encrypt(unencryptedPassword1)

        // when
        val result = passwords.areEquals(encrypted, unencryptedPassword2)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.INTEGRATION])
    fun passwords_do_match() {
        // given
        val unencryptedPassword = UnencryptedPassword("myRawPass")
        val encrypted = passwords.encrypt(unencryptedPassword)

        // when
        val result = passwords.areEquals(encrypted, unencryptedPassword)

        // then
        assertThat(result).isTrue
    }
}