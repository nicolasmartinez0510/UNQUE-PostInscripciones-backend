package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.Formulario
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre

data class FormularioDTO(
        val id: Long,
        val legajoAlumno: Int,
        val cuatrimestre: Cuatrimestre,
        val solicitudes: List<SolicitudSobrecupoDTO>
) {
    companion object {
        fun desdeModelo(formulario: Formulario, legajo: Int): FormularioDTO {
            return FormularioDTO(
                    formulario.id!!,
                    legajo,
                    formulario.cuatrimestre,
                    formulario.solicitudes.map { SolicitudSobrecupoDTO.desdeModelo(it) }
            )
        }
    }

}
