package ar.edu.unq.postinscripciones.model

import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import java.time.Year
import javax.persistence.*

@Entity
class Formulario(
    @ManyToOne(fetch = FetchType.EAGER)
    val cuatrimestre: Cuatrimestre = Cuatrimestre(Year.of(2009), Semestre.S1),
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val solicitudes: List<SolicitudSobrecupo> = listOf()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
