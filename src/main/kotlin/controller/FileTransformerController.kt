package controller

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import service.FileProcessingService

@RestController
@RequestMapping("/api/files")
class FileTransformerController(
    private var fileProcessingService: FileProcessingService
) {

    @PostMapping("/transform")
    fun transformFile(@RequestParam("file") filePartMono: Mono<FilePart>,
    @RequestParam(defaultValue = "false") skipValidation: Boolean): Mono<ResponseEntity<JsonNode>> {
        return filePartMono.flatMap {
            filePart ->
            fileProcessingService.processFileContent(filePart, skipValidation)
        }
            .map {
                jsonNode -> ResponseEntity.ok().body(jsonNode) }
            .defaultIfEmpty(ResponseEntity.badRequest().build())
            }
    }
