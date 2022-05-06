package ar.edu.unq.postinscripciones.model.comision

import java.time.LocalTime
import javax.persistence.*

@Entity
class Horario(
    @Enumerated(EnumType.STRING)
    val dia: Dia,
    val inicio: LocalTime,
    val fin: LocalTime
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
