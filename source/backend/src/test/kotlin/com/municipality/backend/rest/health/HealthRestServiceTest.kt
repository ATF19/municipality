package com.municipality.backend.rest.health

import com.municipality.backend.domain.service.core.Logger
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import org.testng.annotations.Test


internal class HealthRestServiceTest {

    @Test(groups = [TestGroup.UNIT])
    fun return_healthy() {
        // given
        val restService = HealthRestService(mock(Logger::class.java))

        // when
        val result = restService.check()

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}