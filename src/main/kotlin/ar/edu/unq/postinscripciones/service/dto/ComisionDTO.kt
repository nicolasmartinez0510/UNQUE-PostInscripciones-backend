package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.comision.Comision

data class ComisionDTO(
    val id: Long,
    val numero: Int,
    val materia: String,
    val cuposTotales: Int,
    val sobreCuposTotales: Int,
    val cuposDisponibles: Int
) {

    companion object {
        fun desdeModelo(comision: Comision): ComisionDTO {
            return ComisionDTO(
                comision.id!!,
                comision.numero,
                comision.materia.nombre,
                comision.cuposTotales,
                comision.sobrecuposTotales,
                comision.sobrecuposDisponibles()
            )
        }
    }
}