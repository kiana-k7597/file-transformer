package filetransformer.unit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import io.mockk.*
import filetransformer.model.Person
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import filetransformer.repository.PersonRepository
import filetransformer.service.FileProcessingService
import filetransformer.service.FileValidationService
import java.nio.charset.StandardCharsets
import java.util.*

class FileProcessingServiceUnitTests {
    private val fileValidationService = mockk<FileValidationService>()
    private val personRepository = mockk<PersonRepository>()
    private val fileProcessingService = FileProcessingService(fileValidationService, personRepository)

    private val objectMapper = ObjectMapper()

    @Test
    fun `processFileContent should process valid file correctly`() {
        val fileContent = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"
        val multipartFile = MockMultipartFile("file", fileContent.toByteArray(StandardCharsets.UTF_8))
        val person = Person(UUID.fromString("18148426-89e1-11ee-b9d1-0242ac120002"), "1X1D14", "John Smith", "Likes Apricots", "Rides A Bike", 6.2, 12.1)
        val persons = listOf(person)

        every { personRepository.save(any()) } returns person
        every { personRepository.findAll() } returns persons
        every { fileValidationService.isNotEmptyFile(any()) } just Runs
        every { fileValidationService.isValidFormat(any()) } just Runs

        val result = fileProcessingService.processFileContent(multipartFile, false)

        verify { fileValidationService.isNotEmptyFile(any()) }
        verify(exactly = 1) { personRepository.save(any()) }
        verify { personRepository.findAll() }

        val rootNode = objectMapper.readTree(result) as ArrayNode
        assertEquals(1, rootNode.size())
    }

    @Test
    fun `processFileContent should process multiple lines correctly`() {
        val fileContent = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1 \n3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5"

        val multipartFile = MockMultipartFile("file", fileContent.toByteArray(StandardCharsets.UTF_8))
        val person1 = Person(UUID.fromString("18148426-89e1-11ee-b9d1-0242ac120002"), "1X1D14", "John Smith", "Likes Apricots", "Rides A Bike", 6.2, 12.1)
        val person2 = Person(UUID.fromString("3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7"), "2X2D24", "Mike Smith", "Likes Grape", "Drives an SUV", 35.0, 95.5)
        val persons = listOf(person1, person2)

        every { personRepository.save(any()) } returns person1
        every { personRepository.findAll() } returns persons
        every { fileValidationService.isNotEmptyFile(any()) } just Runs
        every { fileValidationService.isValidFormat(any()) } just Runs

        val result = fileProcessingService.processFileContent(multipartFile, false)

        verify { fileValidationService.isNotEmptyFile(any()) }
        verify(exactly = 2) { personRepository.save(any()) }
        verify { personRepository.findAll() }

        val rootNode = objectMapper.readTree(result) as ArrayNode
        assertEquals(2, rootNode.size())
    }

    @Test
    fun `processFileContent should log warning for empty file`() {
        val emptyFile = MockMultipartFile("file", ByteArray(0))

        every { fileValidationService.isNotEmptyFile(any()) } throws IllegalArgumentException("The file is empty")

        assertThrows<IllegalArgumentException> {
            fileProcessingService.processFileContent(emptyFile, false)
        }

        verify { fileValidationService.isNotEmptyFile(any()) }
        verify(exactly = 0) { personRepository.save(any()) }
    }
}