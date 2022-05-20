package ar.edu.unq.postinscripciones.model

enum class EstadoMateria {
    APROBADO, DESAPROBADO, PA;

    companion object {
        fun getByValue(value: Int) = EstadoMateria.values().find { it.ordinal == value }
    }

}