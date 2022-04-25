package ar.edu.unq.postinscripciones.model.comision

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import javax.persistence.*

@Entity
// Revisar como poder hacer andar esto, no anda al querer crear una nueva comision
//@IdClass(ComisionId::class)
class Comision(
    @ManyToOne(fetch = FetchType.EAGER)
    val materia: Materia = Materia("", ""),
    val numero: Int = 1,
    @ManyToOne(fetch = FetchType.EAGER)
    val cuatrimestre: Cuatrimestre = Cuatrimestre(2009, Semestre.S1),
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    val horarios: List<Horario> = listOf(),
    val cuposTotales: Int = 30,
    val cuposOcupados: Int = 0,
    val sobrecuposTotales: Int = 5
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    private var sobrecuposOcupados = 0

    fun cuposDisponibles() = (cuposTotales + sobrecuposTotales) - (cuposOcupados + sobrecuposOcupados)

    fun esLaComision(comision: Comision): Boolean {
        return cuatrimestre.esElCuatrimestre(comision.cuatrimestre) &&
                this.coincideEn(comision.materia, comision.numero)
    }

    private fun coincideEn(materia: Materia, numero: Int) =
        this.materia.esLaMateria(materia) && this.numero == numero
}