package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Modalidad

data class ComisionParaAlumno(
    val id: Long,
    val comision: Int,
    val modalidad: Modalidad,
    val horarios: List<HorarioDTO>
    ) {
    companion object {
        fun desdeModelo(comision: Comision): ComisionParaAlumno {
            return ComisionParaAlumno(
                comision.id!!,
                comision.numero,
                comision.modalidad,
                comision.horarios.map { HorarioDTO.desdeModelo(it) }
            )
        }
    }
}