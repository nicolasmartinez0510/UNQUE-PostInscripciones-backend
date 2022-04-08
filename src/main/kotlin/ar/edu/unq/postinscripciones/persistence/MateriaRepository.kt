package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Materia
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MateriaRepository: CrudRepository<Materia, Long>
