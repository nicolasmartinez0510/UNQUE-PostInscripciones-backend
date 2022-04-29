package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.comision.Dia
import java.time.LocalTime

data class HorarioDTO(val dia: Dia, val inicio: LocalTime, val fin: LocalTime)