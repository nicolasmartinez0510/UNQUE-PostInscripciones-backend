package ar.edu.unq.postinscripciones.service.dto

data class ComisionACrear(
    val numeroComision: Int,
    val codigoMateria: String,
    val horarios: List<HorarioDTO>,
    val cuposTotales: Int,
    val cuposOcupados: Int,
    val sobrecuposTotales: Int
)
