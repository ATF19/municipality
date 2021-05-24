package com.municipality.backend.shared_code_for_tests

import com.municipality.backend.infrastructure.security.SessionAuthentication
import com.municipality.backend.infrastructure.springboot.MunicipalityBackendApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeSuite

@SpringBootTest(classes = [ MunicipalityBackendApplication::class ])
abstract class AbstractIntegrationTest : AbstractTestNGSpringContextTests() {

    @BeforeSuite(groups = [TestGroup.INTEGRATION])
    fun init() {
        SecurityContextHolder.getContext().authentication = SessionAuthentication(TestUser())
    }

}