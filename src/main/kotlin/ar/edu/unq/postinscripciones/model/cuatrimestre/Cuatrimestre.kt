package ar.edu.unq.postinscripciones.model.cuatrimestre

import java.time.Year
import javax.persistence.*

@Entity
@IdClass(CuatrimestreId::class)
class Cuatrimestre(
    @Id
    val anio: Year = Year.of(2020),
    @Id
    @Enumerated(EnumType.STRING)
    val semestre: Semestre = Semestre.S1
) {

    fun esElCuatrimestre(anio: Cuatrimestre) = this.esElCuatrimestre(anio.anio.value, anio.semestre)

    fun esElCuatrimestre(anio: Int, semestre: Semestre) =
        this.anio.value == anio && this.semestre == semestre

}