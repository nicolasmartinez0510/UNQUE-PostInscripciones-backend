package ar.edu.unq.postinscripciones.model.cuatrimestre

import java.time.Year
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass

@Entity
@IdClass(CuatrimestreId::class)
class Cuatrimestre(
    @Id
    val anio: Year,
    @Id
    val semestre: Semestre
) {

    fun esElCuatrimestre(anio: Int, semestre: Semestre) =
        this.anio.value == anio && this.semestre == semestre

}