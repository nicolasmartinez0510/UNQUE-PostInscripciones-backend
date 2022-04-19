package ar.edu.unq.postinscripciones.model.oferta.academica

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Horario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Year

internal class OfertaAcademicaTest {
    lateinit var ofertaAcademica: OfertaAcademica
    lateinit var oferta: List<Comision>

    @BeforeEach
    fun setUp() {
        oferta = listOf(
            comision("1050", 1),
            comision("9090",3)
        )
        ofertaAcademica = OfertaAcademica(Year.of(2009), Cuatrimestre.S2, oferta)
    }

    @Test
    fun `una oferta academica conoce su anio`() {
        assertThat(ofertaAcademica.anio).isEqualTo(Year.of(2009))
    }

    @Test
    fun `una oferta academica conoce su cuatrimestre`() {
        assertThat(ofertaAcademica.cuatrimestre).isEqualTo(Cuatrimestre.S2)
    }

    @Test
    fun `una oferta academica conoce las comisiones que oferta durante ese cuatrimestre`() {
        assertThat(ofertaAcademica.oferta).usingRecursiveComparison().isEqualTo(oferta)
    }

    fun comision(codigoMateria: String, numeroComision: Int, horarios: List<Horario> = listOf()): Comision {
        return Comision(Materia(codigo = codigoMateria), numeroComision, horarios)
    }
}