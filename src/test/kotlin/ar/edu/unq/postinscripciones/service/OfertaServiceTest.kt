package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Materia
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class OfertaServiceTest {

    @Autowired
    private lateinit var ofertaService: OfertaService

    @Autowired
    private lateinit var materiaService: MateriaService

    private lateinit var bdd: Materia

    @BeforeEach
    fun setUp() {
        bdd = materiaService.crear("Base de datos", "BD-109")
    }

    @Test
    fun `Se puede crear una oferta academica para una materia`() {
        val oferta = ofertaService.crearOfertaPara(bdd.codigo, 0)

        assertThat(oferta).isNotNull
    }

    @Test
    fun `Se puede crear una oferta academica para una materia con una capacidad de cupos determinada`() {
        val oferta = ofertaService.crearOfertaPara(bdd.codigo, 20)

        assertThat(oferta.cuposTotales).isEqualTo(20)
    }

    @Test
    fun `No puede crear una oferta academica para una materia que no existe`() {
        val codigoInexistente = "AF-105"

        val exception = assertThrows<RuntimeException> { ofertaService.crearOfertaPara(codigoInexistente, 0) }

        assertThat(exception.message).isEqualTo("La materia no existe")
    }

    @AfterEach
    fun tearDown() {
        ofertaService.clearDataSet()
    }
}