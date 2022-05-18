package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.EstadoFormulario
import ar.edu.unq.postinscripciones.model.Formulario
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre

data class FormularioDTO(
    val id: Long,
    val dniAlumno: Int,
    val cuatrimestre: Cuatrimestre,
    val solicitudes: List<SolicitudSobrecupoDTO>,
    val estado: EstadoFormulario
) {
    companion object {
        fun desdeModelo(formulario: Formulario, dni: Int): FormularioDTO {
            return FormularioDTO(
                    formulario.id!!,
                    dni,
                    formulario.cuatrimestre,
                    formulario.solicitudes.map { SolicitudSobrecupoDTO.desdeModelo(it) },
                    formulario.estado
            )
        }
    }

}
