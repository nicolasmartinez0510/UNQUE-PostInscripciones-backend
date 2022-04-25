package ar.edu.unq.postinscripciones.model.comision

import java.io.Serializable

class ComisionId(
    val codigoMateria: String,
    val numero: Int,
    val cuatrimestreId: Long
) : Serializable
