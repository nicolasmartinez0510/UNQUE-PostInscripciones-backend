package ar.edu.unq.postinscripciones.service.dto

data class MateriaComision(
    val codigo: String,
    val nombre: String,
    val comisiones: MutableList<ComisionParaAlumno>
)