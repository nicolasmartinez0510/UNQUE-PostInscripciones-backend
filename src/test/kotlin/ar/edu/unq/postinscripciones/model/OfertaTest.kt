package ar.edu.unq.postinscripciones.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OfertaTest {

    private lateinit var bdd: Materia
    private lateinit var ofertaBdd: Oferta

    @BeforeEach
    fun setUp() {
        bdd = Materia()
        ofertaBdd = Oferta(bdd, 1)
    }

    @Test
    fun `una oferta pertenece a una materia`() {
        assertThat(ofertaBdd.materia).isEqualTo(bdd)
    }

    @Test
    fun `se puede tomar un cupo disponible de una oferta de materia`() {
        val cuposAntesDeTomarUno = ofertaBdd.cuposDisponibles()

        ofertaBdd.tomarCupo()

        assertThat(ofertaBdd.cuposDisponibles()).isEqualTo(cuposAntesDeTomarUno - 1)
    }

    @Test
    fun `no se pueden tomar un cupo disponible de una oferta de materia`() {
        ofertaBdd.tomarCupo()

        val excepcion = assertThrows<RuntimeException> { ofertaBdd.tomarCupo() }

        assertThat(excepcion.message).isEqualTo("No hay cupos disponibles")
    }
}