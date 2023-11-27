package service

import org.springframework.stereotype.Service

@Service
class FileValidationService {
    fun isValidPersonData(parts: List<String>) {
        val fieldsSize = parts.size
        require(fieldsSize == 7) {
            throw IllegalArgumentException("The file contains $fieldsSize fields, the acceptable number is 7")
        }
    }
}