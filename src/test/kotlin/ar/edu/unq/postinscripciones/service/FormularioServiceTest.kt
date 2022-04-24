package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Dia
import ar.edu.unq.postinscripciones.model.comision.Horario
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.CuatrimestreId
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.resources.DataService
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalTime

@IntegrationTest
class FormularioServiceTest {

    @Autowired
    private lateinit var formularioService: FormularioService

    @Autowired
    private lateinit var cuatrimestreService: CuatrimestreService

    @Autowired
    private lateinit var comisionService: ComisionService

    @Autowired
    private lateinit var materiaService: MateriaService

    @Autowired
    private lateinit var dataService: DataService

    private lateinit var materia: Materia
    private lateinit var c1: Cuatrimestre
    private lateinit var horarios: List<Horario>
    private lateinit var comision: Comision

    @BeforeEach
    fun setUp() {
        materia = materiaService.crear("Base de datos", "BBD-208")
        c1 = cuatrimestreService.crear(2022, Semestre.S1)

        horarios = listOf(
            Horario(Dia.LUNES, LocalTime.of(18, 30, 0), LocalTime.of(21, 30, 0)),
            Horario(Dia.JUEVES, LocalTime.of(18, 30, 0), LocalTime.of(21, 30, 0))
        )

        val formularioComision =  FormularioComision(
            1,
            materia.codigo,
            2022,
            Semestre.S1,
            35,
            5,
            horarios
        )
        comision = comisionService.crear(formularioComision)
    }
    @Test
    fun `Se puede crear un formulario para el cuatrmiestre actual`() {
        val formulario = formularioService.crear(CuatrimestreId(c1.anio, c1.semestre), listOf(Solicitud(comision.id!!), Solicitud(comision.id!!)))

        assertThat(formulario).isNotNull
    }

    @Test
    fun `El Formulario creado tiene la comision adecuada`() {
        val solicitudes = listOf(Solicitud(comision.id!!))
        val formulario = formularioService.crear(CuatrimestreId(c1.anio, c1.semestre), solicitudes)

        assertThat(formulario.solicitudes.first().comision.id).isEqualTo(solicitudes.first().comisionId)
    }

    @AfterEach
    fun tearDown() {
        dataService.clearDataSet()
    }
}