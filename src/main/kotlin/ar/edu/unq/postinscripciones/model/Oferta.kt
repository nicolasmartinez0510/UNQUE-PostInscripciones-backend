package ar.edu.unq.postinscripciones.model

import javax.persistence.*

@Deprecated(
    level = DeprecationLevel.WARNING,
    message = "Ya no forma parte del dominio. Borrarla junto con sus tests y servicios."
)
@Entity
class Oferta(
    @ManyToOne(fetch = FetchType.EAGER, cascade = arrayOf(CascadeType.REMOVE))
    val materia: Materia,
    val cuposTotales: Int = 30
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    private var cuposOcupados = 0

    fun tomarCupo() {
        if (cuposOcupados == cuposTotales) {
            throw SinCuposException()
        }
        cuposOcupados++
    }

    fun cuposDisponibles() = cuposTotales - cuposOcupados
}

class SinCuposException : RuntimeException("No hay cupos disponibles")