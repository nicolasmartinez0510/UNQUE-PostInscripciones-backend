package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ComisionRespository : CrudRepository<Comision, Long> {

    fun findAllByMateria(materia: Materia): List<Comision>
    fun findByCuatrimestreAnioAndCuatrimestreSemestre(anio: Int, semestre: Semestre): List<Comision>
    fun findByNumeroAndMateriaAndCuatrimestre(
        numeroComision: Int,
        materia: Materia,
        cuatrimestre: Cuatrimestre
    ): Optional<Comision>
}