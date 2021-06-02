package com.municipality.backend.infrastructure.persistence.repository

import com.municipality.backend.domain.model.file.ContentType
import com.municipality.backend.domain.model.file.File
import com.municipality.backend.domain.model.file.FileName
import com.municipality.backend.domain.service.file.Files
import com.municipality.backend.shared_code_for_tests.AbstractIntegrationTest
import com.municipality.backend.shared_code_for_tests.TestGroup
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

class FileRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var files: Files

    @Test(groups = [TestGroup.INTEGRATION])
    fun save_and_retrieve_file() {
        // given
        val file = File()
        val fileName = FileName("TestFile")
        val contentType = ContentType("Test")
        val content = "DummyContent"
        file.fileName = fileName
        file.contentType = contentType
        file.blob = content.toByteArray()

        // then
        files.save(file)

        // when
        val byId = files.by(file.id)
        assertThat(byId.fileName).isEqualTo(fileName)
        assertThat(byId.contentType).isEqualTo(contentType)
        assertThat(String(byId.blob)).isEqualTo(content)
    }
}