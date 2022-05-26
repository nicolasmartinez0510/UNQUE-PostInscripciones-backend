package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.comision.Horario
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HorarioRepository: CrudRepository<Horario, Long> {
}