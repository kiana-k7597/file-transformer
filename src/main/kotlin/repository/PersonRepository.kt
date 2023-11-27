package repository

import model.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PersonDetailRepository : JpaRepository<Person, Long> {
}