package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.EstadoSolicitud
import ar.edu.unq.postinscripciones.model.SolicitudSobrecupo
import io.swagger.annotations.ApiModelProperty

data class SolicitudSobrecupoDTO(
        @ApiModelProperty(example = "3332")
        val id: Long,
        @ApiModelProperty(example = "PENDIENTE")
        val estado: EstadoSolicitud,
        val comisionId: Long
) {
    companion object {
        fun desdeModelo(solicitudSobrecupo: SolicitudSobrecupo): SolicitudSobrecupoDTO {
            return SolicitudSobrecupoDTO(
                    solicitudSobrecupo.id!!,
                    solicitudSobrecupo.estado,
                    solicitudSobrecupo.comision.id!!
            )
        }
    }

}
