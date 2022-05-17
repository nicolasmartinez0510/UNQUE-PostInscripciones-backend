package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Carrera
import ar.edu.unq.postinscripciones.model.Materia
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MateriaRepository: CrudRepository<Materia, String> {

    fun findMateriaByCodigo(codigo: String): Optional<Materia>
    fun findByNombreIgnoringCaseOrCodigoIgnoringCase(nombre: String, codigo: String): Optional<Materia>
    fun findAllByCodigoIn(materias: List<String>): List<Materia>
    @Query(
        "FROM Materia as m " +
                "WHERE (m NOT IN ?1) " +
                "AND ((SELECT count(c) FROM m.correlativas as c WHERE c NOT IN ?1)) = 0 " +
                "AND (m.carrera = ?2 OR (m.carrera = ar.edu.unq.postinscripciones.model.Carrera.SIMULTANEIDAD))"
    )
    fun findMateriasDisponibles(materiasAprobadas : List<Materia>, carreraAlumno: Carrera) : List<Materia>
}
