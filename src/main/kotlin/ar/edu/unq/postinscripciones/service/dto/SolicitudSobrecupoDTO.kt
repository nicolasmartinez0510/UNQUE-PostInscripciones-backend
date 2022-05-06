package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.EstadoSolicitud
import ar.edu.unq.postinscripciones.model.SolicitudSobrecupo

data class SolicitudSobrecupoDTO(
        val id: Long,
        val estado: EstadoSolicitud,
        val comisionDTO: ComisionDTO
) {
    companion object {
        fun desdeModelo(solicitudSobrecupo: SolicitudSobrecupo): SolicitudSobrecupoDTO {
            return SolicitudSobrecupoDTO(
                    solicitudSobrecupo.id!!,
                    solicitudSobrecupo.estado,
                    ComisionDTO.desdeModelo(solicitudSobrecupo.comision)
            )
        }
    }

}
