package ar.edu.unq.postinscripciones.model.oferta.academica

import java.io.Serializable
import java.time.Year

data class OfertaAcademicaId(val anio: Year, val cuatrimestre: Cuatrimestre): Serializable