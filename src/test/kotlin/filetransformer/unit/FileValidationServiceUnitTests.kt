package filetransformer.unit

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import filetransformer.service.FileValidationService

class FileValidationServiceUnitTests {

    private val fileValidationService = FileValidationService()

    @Test
    fun `isNotEmptyFile should throw IllegalArgumentException for empty file`() {
        val emptyFile = MockMultipartFile("file", ByteArray(0))

        val exception = assertThrows<IllegalArgumentException> {
            fileValidationService.isNotEmptyFile(emptyFile)
        }

        assertEquals("The file is empty", exception.message)
    }

    @Test
    fun `isNotEmptyFile should not throw an exception for non-empty file`() {
        val nonEmptyFile = MockMultipartFile("file", "filename.txt", "text/plain", "content".toByteArray())

        assertDoesNotThrow {
            fileValidationService.isNotEmptyFile(nonEmptyFile)
        }
    }

    @Test
    fun `isValidFormat should throw IllegalArgumentException for incorrect number of parts`() {
        val parts = listOf("123e4567-e89b-12d3-a456-426655440000", "123456", "Name", "Likes", "Transport", "10.5")

        val exception = assertThrows<IllegalArgumentException> {
            fileValidationService.isValidFormat(parts)
        }

        assertEquals("The file contains must contain exactly 7 fields, current size is 6", exception.message)
    }

    @Test
    fun `isValidFormat should throw IllegalArgumentException for invalid UUID`() {
        val parts = listOf("invalid-uuid", "123456", "Name", "Likes", "Transport", "10.5", "200.0")

        val exception = assertThrows<IllegalArgumentException> {
            fileValidationService.isValidFormat(parts)
        }

        assertEquals("Invalid UUID format.", exception.message)
    }

    @Test
    fun `isValidFormat should throw IllegalArgumentException for incorrect ID length`() {
        val parts = listOf("123e4567-e89b-12d3-a456-426655440000", "1234", "Name", "Likes", "Transport", "10.5", "200.0")

        val exception = assertThrows<IllegalArgumentException> {
            fileValidationService.isValidFormat(parts)
        }

        assertEquals("ID length must be exactly 6 characters; current length is 4.", exception.message)
    }

    @Test
    fun `isValidFormat should validate all fields correctly for valid data`() {
        val parts = listOf("123e4567-e89b-12d3-a456-426655440000", "123456", "Valid Name", "Some Likes", "Car", "50.0", "200.0")

        assertDoesNotThrow {
            fileValidationService.isValidFormat(parts)
        }
    }

    @Test
    fun `isValidFormat should not throw an exception for valid data`() {
        val parts = listOf("123e4567-e89b-12d3-a456-426655440000", "123456", "Valid Name", "Some Likes", "Car", "50.0", "200.0")

        assertDoesNotThrow {
            fileValidationService.isValidFormat(parts)
        }
    }

    @Test
    fun `isValidFormat should throw IllegalArgumentException for invalid name`() {
        val parts = listOf("123e4567-e89b-12d3-a456-426655440000", "123456", "", "Likes", "Transport", "10.5", "200.0")

        val exception = assertThrows<IllegalArgumentException> {
            fileValidationService.isValidFormat(parts)
        }

        assertEquals("Name cannot be blank", exception.message)
    }

    @Test
    fun `isValidFormat should throw IllegalArgumentException for invalid likes`() {
        val longLikes = "L".repeat(256)
        val parts = listOf("123e4567-e89b-12d3-a456-426655440000", "123456", "Name", longLikes, "Transport", "10.5", "200.0")

        val exception = assertThrows<IllegalArgumentException> {
            fileValidationService.isValidFormat(parts)
        }

        assertEquals("Likes string must be under 255 characters, current Likes string is 256", exception.message)
    }

    @Test
    fun `isValidFormat should throw IllegalArgumentException for invalid transport`() {
        val longTransport = "T".repeat(256)
        val parts = listOf("123e4567-e89b-12d3-a456-426655440000", "123456", "Name", "Likes", longTransport, "10.5", "200.0")

        val exception = assertThrows<IllegalArgumentException> {
            fileValidationService.isValidFormat(parts)
        }

        assertEquals("Transport string must be under 255 characters, current Likes string is 256", exception.message)
    }

    @Test
    fun `isValidFormat should throw IllegalArgumentException for negative average speed`() {
        val parts = listOf("123e4567-e89b-12d3-a456-426655440000", "123456", "Name", "Likes", "Transport", "-10.5", "200.0")

        val exception = assertThrows<IllegalArgumentException> {
            fileValidationService.isValidFormat(parts)
        }

        assertEquals("Average speed must be non-negative", exception.message)
    }

    @Test
    fun `isValidFormat should throw IllegalArgumentException for too high top speed`() {
        val parts = listOf("123e4567-e89b-12d3-a456-426655440000", "123456", "Name", "Likes", "Transport", "10.5", "1000.0")

        val exception = assertThrows<IllegalArgumentException> {
            fileValidationService.isValidFormat(parts)
        }

        assertEquals("Top speed must be less than 1000", exception.message)
    }
}
