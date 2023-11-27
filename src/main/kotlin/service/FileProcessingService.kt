package service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class FileProcessingService {

   fun processFileContent(filePart: FilePart, skipValidation: Boolean): Mono<JsonNode> {

    }
}