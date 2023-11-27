package service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.charset.StandardCharsets

@Service
class FileProcessingService {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun processFileContent(file: MultipartFile, skipValidation: Boolean): JsonNode {
        if (file.isEmpty) {
            log.warn("The file requested for processing is empty")
        }

        val fileContent = String(file.bytes, StandardCharsets.UTF_8)
        val rootNode = JsonNodeFactory.instance.arrayNode()
        fileContent.lines().forEach { _ ->
        }
        return rootNode
    }
}