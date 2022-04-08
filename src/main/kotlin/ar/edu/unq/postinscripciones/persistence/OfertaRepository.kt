package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Oferta
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OfertaRepository: CrudRepository<Oferta, Long>
