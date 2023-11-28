package controller

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import service.FileProcessingService
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/files")
class FileTransformerController(
    private var fileProcessingService: FileProcessingService,
) {

    @PostMapping("/transform")
    fun transformFile(
        @RequestParam("file") file: MultipartFile,
        @RequestParam(defaultValue = "false") skipValidation: Boolean,
        response: HttpServletResponse
    ) {
        val outcomeFile = fileProcessingService.processFileContent(file, skipValidation)

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"outcomeFile.json\"")

        response.outputStream.use { os ->
            os.write(outcomeFile)
            os.flush()
        }
    }
}

