package ar.edu.unq.postinscripciones.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OfertaServiceTest {
    @Autowired
    private lateinit var ofertaService: OfertaService

    @Autowired
    private lateinit var materiaService: MateriaService

    @Test
    fun `se puede crear una oferta para una materia con una cantidad de cupos totales`() {
        val bdd = materiaService.crear("Bases de Datos", "PW-1050")
        val cuposTotalesSolicitados = 30

        val oferta = ofertaService.crearOfertaPara(bdd.id!!, cuposTotalesSolicitados)

        assertThat(oferta.materia.id).isEqualTo(bdd.id)
        assertThat(oferta.cuposTotales).isEqualTo(cuposTotalesSolicitados)
    }

    @Test
    fun `se puede asignar un cupo disponible de una oferta`() {
        val bdd = materiaService.crear("Bases de Datos", "PW-1050")
        val cuposTotalesSolicitados = 30
        val oferta = ofertaService.crearOfertaPara(bdd.id!!, cuposTotalesSolicitados)

        ofertaService.asignarCupo(bdd.id!!)

        assertThat(oferta.materia.id).isEqualTo(bdd.id)
        assertThat(oferta.cuposTotales).isEqualTo(cuposTotalesSolicitados)
    }

}