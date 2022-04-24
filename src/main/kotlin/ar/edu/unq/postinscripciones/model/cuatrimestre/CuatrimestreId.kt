package ar.edu.unq.postinscripciones.model.cuatrimestre

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class CuatrimestreId(
    @Column(name = "anio")
    val anio: Int,
    @Column(name = "semestre")
    val semestre: Semestre
): Serializable