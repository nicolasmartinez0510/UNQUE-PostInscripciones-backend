package ar.edu.unq.postinscripciones.model.comision

import ar.edu.unq.postinscripciones.model.cuatrimestre.CuatrimestreId
import java.io.Serializable
import javax.persistence.*

class ComisionId(
    val codigoMateria: String,
    val numero: Int,
    val cuatrimestreId: CuatrimestreId
): Serializable
