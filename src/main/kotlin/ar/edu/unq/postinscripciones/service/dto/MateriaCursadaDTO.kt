package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.EstadoMateria
import ar.edu.unq.postinscripciones.model.MateriaCursada
import java.time.LocalDate

data class MateriaCursadaDTO(
        val codigoMateria: String,
        val estado: EstadoMateria,
        val fechaDeCarga: LocalDate
) {
    companion object {
        fun desdeModelo(materiaCursada: MateriaCursada): MateriaCursadaDTO {
            return MateriaCursadaDTO(
                    materiaCursada.materia.codigo,
                    materiaCursada.estado,
                    materiaCursada.fechaDeCarga
            )
        }
    }

}