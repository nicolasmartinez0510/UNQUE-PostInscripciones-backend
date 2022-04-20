package ar.edu.unq.postinscripciones.model.cuatrimestre

import java.time.Year

data class CuatrimestreId(
    val anio: Year,
    val semestre: Semestre
)