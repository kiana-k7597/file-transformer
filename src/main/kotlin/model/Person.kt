package model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Person(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID,

    val name: String,
    val transport: String,
    val topSpeed: String
)