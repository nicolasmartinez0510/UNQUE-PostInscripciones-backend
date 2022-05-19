package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Alumno
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AlumnoRepository : CrudRepository<Alumno, Int> {
    fun findByDniOrLegajo(dni: Int, legajo: Int): Optional<Alumno>
    fun findByDni(dni: Int): Optional<Alumno>
    fun findByFormulariosSolicitudesComisionId(idComision: Long): List<Alumno>
}