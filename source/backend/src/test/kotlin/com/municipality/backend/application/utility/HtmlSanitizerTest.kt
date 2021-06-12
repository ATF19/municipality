package com.municipality.backend.application.utility

import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class HtmlSanitizerTest {

    @Test(groups = [TestGroup.UNIT])
    fun remove_js_script_from_html() {
        // given
        val html = "<p>atef<script>alert('x')</script></p><h1 style=\"background-color: red;\">demo</h1>"

        // when
        val result = HtmlSanitizer.sanitze(html)

        // then
        assertThat(result).isEqualTo("<p>atef</p><h1 style=\"background-color: red;\">demo</h1>")
    }
}