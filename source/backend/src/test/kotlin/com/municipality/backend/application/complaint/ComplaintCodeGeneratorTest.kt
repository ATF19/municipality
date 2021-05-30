package com.municipality.backend.application.complaint

import com.municipality.backend.domain.service.complaint.Complaints
import com.municipality.backend.shared_code_for_tests.TestGroup
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class ComplaintCodeGeneratorTest {

    @Test(groups = [TestGroup.UNIT])
    fun generate_random_code() {
        // given
        val complaints = mockk<Complaints>()
        val generator = ComplaintCodeGenerator(complaints)
        every { complaints.exists(any()) }.returns(false)

        // when
        val code = generator.generate()

        // then
        assertThat(code.code).isNotNull
        assertThat(code.code).hasSize(8)
    }

}