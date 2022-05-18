package ar.edu.unq.postinscripciones.service.dto

import ar.edu.unq.postinscripciones.model.EstadoMateria
import ar.edu.unq.postinscripciones.model.MateriaCursada
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDate

data class MateriaCursadaDTO(
    @ApiModelProperty(example = "00054", required = true)
    val codigoMateria: String,
    @ApiModelProperty(example = "DESAPROBADO", required = true)
    val estado: EstadoMateria,
    @ApiModelProperty(example = "2022-07-30T23:59", required = true)
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