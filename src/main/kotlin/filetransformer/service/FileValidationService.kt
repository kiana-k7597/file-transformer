package filetransformer.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class FileValidationService {

    fun isNotEmptyFile(file: MultipartFile) {
        require(!(file.isEmpty)) { "The file is empty" }
    }

    fun isValidFormat(parts: List<String>) {
        isValidSize(parts)

        validateUUID(parts[0])
        validateId(parts[1])
        validateName(parts[2])
        validateLikes(parts[3])
        validateTransport(parts[4])
        validateSpeed(parts[5], "Average speed")
        validateSpeed(parts[6], "Top speed")
    }

    private fun validateUUID(uuid: String) {
        try {
            UUID.fromString(uuid)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid UUID format.")
        }
    }

    private fun validateId(id: String) {
        val idLength = id.length
        require(id.isNotBlank()) { "ID cannot be blank" }
        require(idLength == 6) {"ID length must be exactly 6 characters; current length is $idLength."}
    }

    fun isValidSize(parts: List<String>) {
        val fieldsSize = parts.size
        require(fieldsSize == 7) {
            throw IllegalArgumentException("The file contains must contain exactly 7 fields, current size is $fieldsSize")
        }
    }

    private fun validateName(name: String) {
        val nameLength = name.length
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(name.length <= 100) { "Name must be under 100 characters, current name is $nameLength" }
    }

    private fun validateLikes(likes: String) {
        val likesLength = likes.length
        require(likes.isNotBlank()) { "Likes cannot be blank"}
        require(likes.length <= 255) { "Likes string must be under 255 characters, current Likes string is $likesLength" }
    }

    private fun validateTransport(transport: String) {
        val transportLength = transport.length
        require(transport.isNotBlank()) { "Transport cannot be blank" }
        require(transportLength <= 255) {
            "Transport string must be under 255 characters, current Likes string is $transportLength" }
    }

    private fun validateSpeed(speed: String, fieldName: String) {
        val speedValue = speed.toDouble()
        require(speedValue >= 0) { "$fieldName must be non-negative" }
        require(speedValue < 1000) { "$fieldName must be less than 1000" }
    }
}
