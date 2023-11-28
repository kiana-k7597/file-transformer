package filetransformer.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Person(
    @Id
    val uuid: UUID,
    val id: String,
    val name: String,
    val likes: String,
    val transport: String,
    val avgSpeed: Double,
    val topSpeed: Double
)