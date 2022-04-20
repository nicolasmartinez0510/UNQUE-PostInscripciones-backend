package ar.edu.unq.postinscripciones.model.comision

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import java.io.Serializable

data class ComisionId(
    val materia: Materia,
    val numero: Int,
    val cuatrimestre: Cuatrimestre
): Serializable
