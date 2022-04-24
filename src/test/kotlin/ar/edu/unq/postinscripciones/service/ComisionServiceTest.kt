package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Dia
import ar.edu.unq.postinscripciones.model.comision.Horario
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalTime
import java.time.Year

@IntegrationTest
class ComisionServiceTest {

    @Autowired
    private lateinit var comisionService: ComisionService

    @Autowired
    private lateinit var cuatrimestreService: CuatrimestreService

    @Autowired
    private lateinit var materiaService: MateriaService

    private lateinit var bdd: Materia
    private lateinit var c1: Cuatrimestre
    private lateinit var horarios: List<Horario>
    private lateinit var comision: Comision

    @BeforeEach
    fun setUp() {
        bdd = materiaService.crear("Base de datos", "BBD-208")
        c1 = cuatrimestreService.crear(Year.of(2022), Semestre.S1)

        horarios = listOf(
                Horario(Dia.LUNES, LocalTime.of(18, 30, 0),LocalTime.of(21, 30, 0)),
                Horario(Dia.JUEVES, LocalTime.of(18, 30, 0),LocalTime.of(21, 30, 0))
        )

        val formulario =  FormularioComision(
                1,
                bdd.codigo,
                Year.of(2022),
                Semestre.S1,
                35,
                5,
                horarios
        )
        comision = comisionService.crear(formulario)
    }

    @Test
    fun `Se puede crear una comision`() {
        assertThat(comision).isNotNull
    }

//    @Test
//    fun `Se puede pedir todas las comisiones de una materia`() {
//        assertThat(comisionService.obtenerComisionesMateria(bdd.codigo).first()).usingRecursiveComparison()
//                .isEqualTo(comision)
//    }
//    @Test
//    fun `Se puede pedir la cantidad de cupos de una comision`() {
//        val cuposDisponibles =
//    }

    @AfterEach
    fun tearDown() {
        comisionService.clearDataSet()
    }

}