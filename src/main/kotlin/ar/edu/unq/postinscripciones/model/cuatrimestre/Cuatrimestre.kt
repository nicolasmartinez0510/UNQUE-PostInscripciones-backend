package ar.edu.unq.postinscripciones.model.cuatrimestre

import java.io.Serializable
import javax.persistence.*

@Entity
@IdClass(CuatrimestreId::class)
class Cuatrimestre(
    @Id
    val anio: Int = 2020,
    @Id
    @Enumerated(EnumType.STRING)
    val semestre: Semestre = Semestre.S1
): Serializable {

    fun esElCuatrimestre(anio: Cuatrimestre) = this.esElCuatrimestre(anio.anio, anio.semestre)

    fun esElCuatrimestre(anio: Int, semestre: Semestre) =
        this.anio == anio && this.semestre == semestre

}