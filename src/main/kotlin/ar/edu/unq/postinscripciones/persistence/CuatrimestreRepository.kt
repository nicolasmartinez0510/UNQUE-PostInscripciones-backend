package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.CuatrimestreId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CuatrimestreRepository: CrudRepository<Cuatrimestre, CuatrimestreId> {


}