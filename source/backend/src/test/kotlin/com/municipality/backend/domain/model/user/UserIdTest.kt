package com.municipality.backend.domain.model.user

import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class UserIdTest {

    @Test(groups = [TestGroup.UNIT])
    fun create_system_user_id() {
        // given

        // when
        val userId = SystemUserId.instance

        // then
        assertThat(userId.isSystemId()).isTrue
        assertThat(userId.isAnonymousId()).isFalse
        assertThat(userId.isRegisteredUserId()).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun create_anonymous_user_id() {
        // given

        // when
        val userId = AnonymousUserId.instance

        // then
        assertThat(userId.isAnonymousId()).isTrue
        assertThat(userId.isSystemId()).isFalse
        assertThat(userId.isRegisteredUserId()).isFalse
    }

    @Test(groups = [TestGroup.UNIT])
    fun create_registered_user_id() {
        // given

        // when
        val userId = RegisteredUserId()

        // then
        assertThat(userId.isRegisteredUserId()).isTrue
        assertThat(userId.isAnonymousId()).isFalse
        assertThat(userId.isSystemId()).isFalse
    }
}