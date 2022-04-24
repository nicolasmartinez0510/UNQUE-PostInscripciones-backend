package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Year

@IntegrationTest
class CuatrimestreServiceTest {

    @Autowired
    private lateinit var cuatrimestreService: CuatrimestreService

    @Test
    fun `Se puede crear un cuatrimestre con el anio y el semestre actual`() {
        val cuatrimestre = cuatrimestreService.crear(Year.of(2022), Semestre.S2)

        assertThat(cuatrimestre).isNotNull
    }
}