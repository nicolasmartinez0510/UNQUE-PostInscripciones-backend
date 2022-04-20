package ar.edu.unq.postinscripciones.model.comision

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import java.time.Year
import javax.persistence.*

@Entity
@IdClass(ComisionId::class)
class Comision(
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    val materia: Materia = Materia("", ""),
    @Id
    val numero: Int = 1,
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    val cuatrimestre: Cuatrimestre = Cuatrimestre(Year.of(2009), Semestre.S1),
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val horarios: List<Horario> = listOf(),
    val cuposTotales: Int = 30,
    val cuposOcupados: Int = 0,
    val sobrecuposTotales: Int = 5
) {
    private var sobrecuposOcupados = 0

    fun cuposDisponibles() = (cuposTotales + sobrecuposTotales) - (cuposOcupados + sobrecuposOcupados)

    fun esLaComision(comision: Comision): Boolean {
        return cuatrimestre.esElCuatrimestre(comision.cuatrimestre) &&
                this.coincideEn(comision.materia, comision.numero)
    }

    private fun coincideEn(materia: Materia, numero: Int) =
        this.materia.esLaMateria(materia) && this.numero == numero
}