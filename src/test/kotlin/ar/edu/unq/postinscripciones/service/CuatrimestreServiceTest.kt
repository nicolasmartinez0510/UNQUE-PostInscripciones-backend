package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.resources.DataService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class CuatrimestreServiceTest {

    @Autowired
    private lateinit var dataService: DataService

    @Autowired
    private lateinit var cuatrimestreService: CuatrimestreService

    @Test
    fun `Se puede crear un cuatrimestre con el anio y el semestre actual`() {
        val cuatrimestre = cuatrimestreService.crear(2022, Semestre.S2)

        assertThat(cuatrimestre).isNotNull
    }

    @Test
    fun `no se pueden repetir cuatrimestres por la combinacion del anio y semestre`() {
        cuatrimestreService.crear(2022, Semestre.S2)

        val excepcion = assertThrows<ExcepcionUNQUE> { cuatrimestreService.crear(2022, Semestre.S2) }

        assertThat(excepcion.message).isEqualTo("Ya existe el cuatrimestre que desea crear.")
    }

    @AfterEach
    fun tearDown() {
        dataService.clearDataSet()
    }
}