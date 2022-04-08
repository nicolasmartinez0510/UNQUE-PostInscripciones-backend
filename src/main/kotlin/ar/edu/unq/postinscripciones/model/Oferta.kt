package ar.edu.unq.postinscripciones.model

import javax.persistence.*

@Entity
class Oferta(
    @ManyToOne(fetch = FetchType.EAGER)
    val materia: Materia,
    val cuposTotales : Int = 30
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    private var cuposOcupados = 0

    fun tomarCupo() {
        if(cuposOcupados == cuposTotales) {
            throw RuntimeException("No hay cupos disponibles")
        }
        cuposOcupados++
    }

    fun cuposDisponibles() = cuposTotales - cuposOcupados
}