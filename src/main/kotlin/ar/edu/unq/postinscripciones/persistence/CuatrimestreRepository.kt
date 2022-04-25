package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CuatrimestreRepository : CrudRepository<Cuatrimestre, Long> {
    fun findByAnioAndSemestre(anio: Int, semestre: Semestre): Optional<Cuatrimestre>
}