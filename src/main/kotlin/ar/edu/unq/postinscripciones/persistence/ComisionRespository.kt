package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ComisionRespository : CrudRepository<Comision, Long> {

    fun findAllByMateria(materia: Materia): List<Comision>
    fun findByCuatrimestreAnioAndCuatrimestreSemestre(anio: Int, semestre: Semestre): List<Comision>
}