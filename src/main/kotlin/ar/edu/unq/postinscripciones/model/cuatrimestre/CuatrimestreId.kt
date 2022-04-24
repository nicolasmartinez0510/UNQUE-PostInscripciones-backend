package ar.edu.unq.postinscripciones.model.cuatrimestre

import java.time.Year
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class CuatrimestreId(
    @Column(name = "anio")
    val anio: Year,
    @Column(name = "semestre")
    val semestre: Semestre
): Serializable