package transformer

import model.Person
import java.util.*

fun transformLineToPerson(line: String): Person {
        val parts = line.split("|")
            return Person(
                id = UUID.fromString(parts[0]),
                name = parts[2],
                transport = parts[4],
                topSpeed = parts[6]
            )
    }
