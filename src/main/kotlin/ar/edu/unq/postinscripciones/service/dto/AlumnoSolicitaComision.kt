package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.Alumno
import ar.edu.unq.postinscripciones.model.comision.Comision
import io.swagger.annotations.ApiModelProperty

data class AlumnoSolicitaComision(
    @ApiModelProperty(example = "1234577")
    val dni: Int,
    @ApiModelProperty(example = "Humberto Prim")
    val nyAp: String,
    @ApiModelProperty(example = "15")
    val cantidadDeAprobadas: Int,
    @ApiModelProperty(example = "1201")
    val idFormulario: Long,
    @ApiModelProperty(example = "3213")
    val idSolicitud: Long
) {
    companion object {
        fun desdeModelo(alu: Alumno, comision: Comision): AlumnoSolicitaComision {
            val formularioYSolicitud = alu.obtenerFormularioYSolicitud(comision)
            return AlumnoSolicitaComision(
                alu.dni,
                "${alu.nombre} ${alu.apellido}",
                alu.cantidadAprobadas(),
                formularioYSolicitud.first.id!!,
                formularioYSolicitud.second.id!!,
            )
        }
    }
}