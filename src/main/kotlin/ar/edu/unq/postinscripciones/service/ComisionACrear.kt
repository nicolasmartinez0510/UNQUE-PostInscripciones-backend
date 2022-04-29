package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.comision.Dia
import java.time.LocalTime

data class ComisionACrear(
    val numeroComision: Int,
    val codigoMateria: String,
    val horarios: List<HorarioDTO>,
    val cuposTotales: Int,
    val cuposOcupados: Int,
    val sobrecuposTotales: Int
)

data class HorarioDTO(val dia: Dia, val inicio: LocalTime, val fin: LocalTime)