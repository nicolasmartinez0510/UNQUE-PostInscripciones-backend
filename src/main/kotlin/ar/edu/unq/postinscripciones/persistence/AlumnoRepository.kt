package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Alumno
import ar.edu.unq.postinscripciones.model.EstadoMateria
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.persistence.Tuple

@Repository
interface AlumnoRepository : CrudRepository<Alumno, Int> {
    fun findByDniOrLegajo(dni: Int, legajo: Int): Optional<Alumno>
    fun findByDni(dni: Int): Optional<Alumno>

    @Query(
            "SELECT cursada.MATERIA_CODIGO, CURSADA.ESTADO, INFO.FECHA, INFO.INTENTOS " +
            "FROM MATERIA_CURSADA AS CURSADA " +
            "JOIN " +
            "(SELECT ALUMNO_DNI, MATERIA_CODIGO, MAX(FECHA_DE_CARGA) as FECHA, COUNT(*) AS INTENTOS " +
            "FROM MATERIA_CURSADA\n" +
            "JOIN ALUMNO_HISTORIA_ACADEMICA " +
            "ON HISTORIA_ACADEMICA_ID = ID " +
            "WHERE ALUMNO_DNI = ?1 " +
            "GROUP BY ALUMNO_DNI, MATERIA_CODIGO) AS INFO " +
            "ON info.MATERIA_CODIGO = cursada.MATERIA_CODIGO " +
            "WHERE INFO.FECHA= CURSADA.FECHA_DE_CARGA",

            nativeQuery = true
    )
    fun findResumenHistoriaAcademica(dni: Int): List<Tuple>
    fun findByFormulariosSolicitudesComisionId(idComision: Long): List<Alumno>
}