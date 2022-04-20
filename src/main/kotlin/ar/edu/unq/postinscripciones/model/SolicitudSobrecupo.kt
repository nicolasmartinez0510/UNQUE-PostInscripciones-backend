package ar.edu.unq.postinscripciones.model

import ar.edu.unq.postinscripciones.model.comision.Comision
import javax.persistence.*

@Entity
class SolicitudSobrecupo(
    @ManyToOne
    val comision: Comision = Comision()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    var estado: EstadoSolicitud = EstadoSolicitud.PENDIENTE
}

enum class EstadoSolicitud {
    PENDIENTE, APROBADO, RECHAZADO
}
