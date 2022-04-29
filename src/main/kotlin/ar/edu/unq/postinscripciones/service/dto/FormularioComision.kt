package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.comision.Horario
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre

data class FormularioComision(
    val numero: Int,
    val codigoMateria: String,
    val anio: Int,
    val semestre: Semestre,
    val cuposTotales: Int,
    val sobreCuposTotales: Int,
    val horarios: List<Horario>
)