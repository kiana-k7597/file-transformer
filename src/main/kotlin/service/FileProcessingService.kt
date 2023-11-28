package service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import model.Person
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import repository.PersonRepository
import transformer.transformLineToPerson
import java.nio.charset.StandardCharsets

@Service
class FileProcessingService(
    private var fileValidationService: FileValidationService,
    private var personRepository: PersonRepository
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    val objectMapper = ObjectMapper()


    fun processFileContent(file: MultipartFile, skipValidation: Boolean): ByteArray {
        if (!skipValidation) {
            fileValidationService.isNotEmptyFile(file)
        }

        val fileContent = String(file.bytes, StandardCharsets.UTF_8)

        savePerson(fileContent, skipValidation)

        val persons = personRepository.findAll()

        val rootNode = getPerson(persons)

        return convertJsonToFileContent(rootNode)
    }

    private fun savePerson(fileContent: String, skipValidation: Boolean){
        fileContent.lines().forEach { line ->
            val parts = line.split("|")
            if(!skipValidation){
            fileValidationService.isValidFormat(parts)
            }

            val person = transformLineToPerson(line)
            personRepository.save(person)
        }
    }
    private fun getPerson(persons: List<Person>): JsonNode{
        val rootNode = JsonNodeFactory.instance.arrayNode()

        persons.forEach { person ->
            val personNode = objectMapper.valueToTree<JsonNode>(person)
            rootNode.add(personNode)
        }

        return rootNode
    }

    private fun convertJsonToFileContent(rootNode: JsonNode): ByteArray {
        val jsonString = objectMapper.writeValueAsString(rootNode)
        return jsonString.toByteArray()
    }
}