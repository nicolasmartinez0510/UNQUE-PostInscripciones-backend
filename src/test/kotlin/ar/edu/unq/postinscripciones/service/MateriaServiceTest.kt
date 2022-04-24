package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Materia
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class MateriaServiceTest {

    @Autowired
    private lateinit var materiaService: MateriaService

    private lateinit var bdd: Materia
    private lateinit var algo: Materia

    @BeforeEach
    fun setUp() {
        bdd = materiaService.crear("Base de datos", "BD-096")
        algo = materiaService.crear("Algoritmos", "AA-208")
    }

    @Test
    fun `Se puede crear una materia`() {
        val materia = materiaService.crear("Intro", "IP-102")
        assertThat(materia).isNotNull
    }

    @Test
    fun `Se pueden obtener todas las materias registradas`() {
        val materiasEsperadas = arrayListOf(bdd, algo)

        assertThat(materiaService.todas()).usingRecursiveComparison().isEqualTo(materiasEsperadas)
    }

    @Test
    fun `Se puede obtener una materia especifica`() {
        val materiaEncontrada = materiaService.obtener(bdd.codigo)
        assertThat(materiaEncontrada).usingRecursiveComparison().isEqualTo(bdd)
    }

    @AfterEach
    fun tearDown() {
        materiaService.clearDataSet()
    }
}