package com.municipality.backend.application.utility

import com.municipality.backend.application.user.UnencryptedPassword
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class PasswordUtilityTest {

    @Test(groups = [TestGroup.UNIT])
    fun return_false_if_password_length_is_less_than_8() {
        // given
        val password = UnencryptedPassword("lA1*_s")

        // when
        val result = PasswordUtility.isStrong(password)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun return_false_if_password_does_not_contain_uppercase() {
        // given
        val password = UnencryptedPassword("lds94380+=dd1*_s")

        // when
        val result = PasswordUtility.isStrong(password)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun return_false_if_password_does_not_contain_lowercase() {
        // given
        val password = UnencryptedPassword("LDS94380+=DD1*_Q")

        // when
        val result = PasswordUtility.isStrong(password)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun return_false_if_password_does_not_contain_number() {
        // given
        val password = UnencryptedPassword("LDSdlsqmkdlsmqC+=DD*_Q")

        // when
        val result = PasswordUtility.isStrong(password)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun return_false_if_password_contain_space() {
        // given
        val password = UnencryptedPassword("LDS9438@+s_ 0XdD1BqaQ")

        // when
        val result = PasswordUtility.isStrong(password)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun return_true_if_password_is_strong() {
        // given
        val password = UnencryptedPassword("LDS9438@+s_0XdD1BqaQ")

        // when
        val result = PasswordUtility.isStrong(password)

        // then
        assertThat(result).isTrue
    }
}