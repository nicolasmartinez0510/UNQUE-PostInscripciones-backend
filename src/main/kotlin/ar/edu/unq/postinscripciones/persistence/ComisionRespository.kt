package ar.edu.unq.postinscripciones.persistence

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.Tuple

@Repository
interface ComisionRespository : CrudRepository<Comision, Long> {

    fun findAllByMateria(materia: Materia): List<Comision>
    fun findByCuatrimestreAnioAndCuatrimestreSemestre(anio: Int, semestre: Semestre): List<Comision>
    fun findByNumeroAndMateriaAndCuatrimestre(
        numeroComision: Int,
        materia: Materia,
        cuatrimestre: Cuatrimestre
    ): Optional<Comision>

    @Query(
        "select c, count(s) as total from Comision as c left join SolicitudSobrecupo s on s.comision.id = c.id where c.cuatrimestre.anio = ?1 and c.cuatrimestre.semestre = ?2 group by c.id order by count(s) desc "
    )
    fun findByCuatrimestreAnioAndCuatrimestreSemestreOrderByCountSolicitudes(anio: Int, semestre: Semestre): List<Tuple>

}