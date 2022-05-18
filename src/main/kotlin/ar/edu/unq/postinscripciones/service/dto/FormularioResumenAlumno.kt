package ar.edu.unq.postinscripciones.service.dto

data class FormularioResumenAlumno(
        val nombre: String,
        val dni: Int,
        val formulario: FormularioDTO,
        val resumenCursadas: List<MateriaCursadaResumenDTO>
)
