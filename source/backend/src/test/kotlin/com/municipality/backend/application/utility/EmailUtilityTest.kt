package com.municipality.backend.application.utility

import com.municipality.backend.domain.model.user.Email
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test


class EmailUtilityTest {

    @Test(groups = [TestGroup.UNIT])
    fun return_false_if_email_does_not_have_at_sign() {
        // given
        val email = Email("bengmail.com")

        // when
        val result = EmailUtility.isValid(email)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun return_false_if_email_does_not_have_dot_sign() {
        // given
        val email = Email("ben@gmailcom")

        // when
        val result = EmailUtility.isValid(email)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun return_false_if_email_does_not_have_string_before_at_sign() {
        // given
        val email = Email("@gmail.com")

        // when
        val result = EmailUtility.isValid(email)

        // then
        assertThat(result).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun return_true_if_provided_with_valid_email() {
        // given
        val email = Email("demo@gmail.com")

        // when
        val result = EmailUtility.isValid(email)

        // then
        assertThat(result).isTrue
    }
}