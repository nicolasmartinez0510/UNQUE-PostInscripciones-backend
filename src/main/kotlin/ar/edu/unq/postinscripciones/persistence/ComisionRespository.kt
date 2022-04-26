package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ComisionRespository : CrudRepository<Comision, Long> {

    fun findAllByMateria(materia: Materia): Optional<List<Comision>>
}