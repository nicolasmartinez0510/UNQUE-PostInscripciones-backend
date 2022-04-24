package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Alumno
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AlumnoRepository: CrudRepository<Alumno, Int> {
}