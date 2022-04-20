package ar.edu.unq.postinscripciones.model

import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Year

internal class FormularioTest {
    lateinit var cuatrimestre: Cuatrimestre
    lateinit var formulario: Formulario
    lateinit var solicitud: SolicitudSobrecupo
    @BeforeEach
    fun `set up`() {
        solicitud = SolicitudSobrecupo()
        cuatrimestre = Cuatrimestre(Year.of(2010), Semestre.S1)
        formulario = Formulario(cuatrimestre = cuatrimestre, solicitudes = listOf(solicitud))
    }

    @Test
    fun `un formulario puede agregar una solicitud de cupo`() {
        assertThat(formulario.solicitudes).containsExactly(solicitud)
    }

    @Test
    fun `un formulario conoce su cuatrimestre`() {
        assertThat(formulario.cuatrimestre).isEqualTo(cuatrimestre)
    }
}
