package ar.edu.unq.postinscripciones.model.comision

import ar.edu.unq.postinscripciones.model.Materia
import javax.persistence.*

@Entity
@IdClass(ComisionId::class)
class Comision(
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    val materia: Materia,
    @Id
    val numero: Int,
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val horarios: List<Horario>,
    val cuposTotales: Int = 30,
    val cuposOcupados: Int = 0,
    val sobrecuposTotales: Int = 5
    ) {
    private var sobrecuposOcupados = 0

    fun cuposDisponibles() = (cuposTotales + sobrecuposTotales) - (cuposOcupados + sobrecuposOcupados)
}