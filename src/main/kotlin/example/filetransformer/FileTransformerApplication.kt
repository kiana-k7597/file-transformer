package example.filetransformer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FileTransformerApplication

fun main(args: Array<String>) {
    runApplication<FileTransformerApplication>(*args)
}
