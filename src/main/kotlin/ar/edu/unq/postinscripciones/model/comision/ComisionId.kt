package ar.edu.unq.postinscripciones.model.comision

import ar.edu.unq.postinscripciones.model.Materia
import java.io.Serializable

data class ComisionId(val materia: Materia, val numero: Int): Serializable