package ar.edu.unq.postinscripciones.model.cuatrimestre

import java.time.Year
import java.io.Serializable

data class CuatrimestreId(
    val anio: Year,
    val semestre: Semestre
): Serializable