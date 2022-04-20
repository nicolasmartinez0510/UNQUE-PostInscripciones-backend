package ar.edu.unq.postinscripciones.model

import javax.persistence.Id

class Director(
    @Id
    val dni: Int = 123,
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val contrasenia: String = ""
) {
    fun decidir(solicitudSobrecupo: SolicitudSobrecupo, decision: EstadoSolicitud) {
        solicitudSobrecupo.estado = decision
    }
}