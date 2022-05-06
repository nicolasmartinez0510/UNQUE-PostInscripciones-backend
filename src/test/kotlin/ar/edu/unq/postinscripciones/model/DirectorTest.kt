package ar.edu.unq.postinscripciones.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DirectorTest {
    private lateinit var solicitudSobrecupo: SolicitudSobrecupo
    private lateinit var falvia: Director

    @BeforeEach
    fun `set up`() {
        falvia = Director()
        solicitudSobrecupo = SolicitudSobrecupo()
    }

    @Test
    fun `un director puede aprobar una solicitud de un sobrecupo`() {
        falvia.decidir(solicitudSobrecupo, EstadoSolicitud.APROBADO)

        assertThat(solicitudSobrecupo.estado).isEqualTo(EstadoSolicitud.APROBADO)
    }

    @Test
    fun `un director puede rechazar una solicitud de un sobrecupo`() {
        falvia.decidir(solicitudSobrecupo, EstadoSolicitud.RECHAZADO)

        assertThat(solicitudSobrecupo.estado).isEqualTo(EstadoSolicitud.RECHAZADO)
    }

    @Test
    fun `un director conoce su dni`() {
        falvia = Director(dni = 4)

        assertThat(falvia.dni).isEqualTo(4)
    }

    @Test
    fun `un director conoce su nombre y apellido`() {
        falvia = Director(nombre = "falvia", apellido = "sangalia")

        assertThat(falvia.nombre).isEqualTo("falvia")
        assertThat(falvia.apellido).isEqualTo("sangalia")
    }

    @Test
    fun `un director conoce su correo y contrasenia`() {
        falvia = Director(correo = "falvia@ejemplo.com", contrasenia = "contra")

        assertThat(falvia.correo).isEqualTo("falvia@ejemplo.com")
        assertThat(falvia.contrasenia).isEqualTo("contra")
    }


}