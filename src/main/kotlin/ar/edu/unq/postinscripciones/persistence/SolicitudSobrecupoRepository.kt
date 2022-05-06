package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.SolicitudSobrecupo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SolicitudSobrecupoRepository: CrudRepository<SolicitudSobrecupo, Long> {
}