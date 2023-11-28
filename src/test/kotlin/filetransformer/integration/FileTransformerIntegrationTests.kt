package filetransformer.integration

import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.ResourceUtils
import java.nio.file.Files

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class FileUploadIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `upload file and process data`() {
        val file = ResourceUtils.getFile("classpath:EntryFile.txt")
        val fileContent = Files.readAllBytes(file.toPath())
        val multipartFile = MockMultipartFile("file", "EntryFile.txt", "text/plain", fileContent)

        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/files/transform")
                .file(multipartFile)
                .param("skipValidation", "false")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"outcomeFile.json\""))
    }

    @Test
    fun `upload empty file should return an error`() {
        val emptyFile = MockMultipartFile("file", "empty.txt", "text/plain", ByteArray(0))

        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/files/transform")
                .file(emptyFile)
                .param("skipValidation", "false")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().string(containsString("The file is empty")))
    }

    @Test
    fun `upload file with invalid format should return an error`() {
        val invalidContent = "invalid-content-without-proper-format".toByteArray()
        val invalidFile = MockMultipartFile("file", "invalid.txt", "text/plain", invalidContent)

        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/files/transform")
                .file(invalidFile)
                .param("skipValidation", "false")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}