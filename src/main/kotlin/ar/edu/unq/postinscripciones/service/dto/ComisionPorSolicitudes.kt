package ar.edu.unq.postinscripciones.service.dto

import javax.persistence.Tuple

data class ComisionPorSolicitudes(
    val id: Long,
    val comision: Int,
    val materia: String,
    val cantidadSolicitudes: Long,
    val sobrecuposTotales: Int,
    val sobrecuposDisponibles: Int
) {
    companion object {
        fun desdeTupla(tuple: Tuple): ComisionPorSolicitudes {
            return ComisionPorSolicitudes(
                tuple.get(0) as Long,
                tuple.get(1) as Int,
                tuple.get(2) as String,
                tuple.get(3) as Long,
                tuple.get(4) as Int,
                tuple.get(5) as Int
            )
        }
    }
}