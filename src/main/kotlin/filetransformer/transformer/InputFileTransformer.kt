package filetransformer.transformer

import filetransformer.model.Person
import java.util.*

fun transformLineToPerson(line: String): Person {
        val parts = line.split("|")
            return Person(
                uuid = UUID.fromString(parts[0]),
                id = parts [1],
                name = parts[2],
                likes = parts[3],
                transport = parts[4],
                avgSpeed = parts[5].toDouble(),
                topSpeed = parts[6].toDouble()
            )
    }
