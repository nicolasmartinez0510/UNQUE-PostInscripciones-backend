package ar.edu.unq.postinscripciones.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Materia(
    @Id
    val codigo: String = "",
    val nombre: String = "",
) {
    fun esLaMateria(materia: Materia) = this.codigo == materia.codigo
}
