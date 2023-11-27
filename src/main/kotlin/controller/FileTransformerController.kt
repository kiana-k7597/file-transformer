package controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import service.FileProcessingService

@RestController
@RequestMapping("/api/files")
class FileTransformerController(
    private var fileProcessingService: FileProcessingService,
) {

    @PostMapping("/transform")
    fun transformFile(
        @RequestParam("file") file: MultipartFile,
        @RequestParam(defaultValue = "false") skipValidation: Boolean
    ): Any {
        return ResponseEntity.ok(
            fileProcessingService.processFileContent(file, skipValidation)
        )
    }
}

