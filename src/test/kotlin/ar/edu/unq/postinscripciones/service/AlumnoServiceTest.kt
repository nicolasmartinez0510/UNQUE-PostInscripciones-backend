package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Alumno
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.model.Formulario
import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Dia
import ar.edu.unq.postinscripciones.model.comision.Horario
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.CuatrimestreId
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.resources.DataService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalTime

@IntegrationTest
class AlumnoServiceTest {


    @Autowired
    private lateinit var formularioService: FormularioService

    @Autowired
    private lateinit var alumnoService: AlumnoService

    @Autowired
    private lateinit var cuatrimestreService: CuatrimestreService

    @Autowired
    private lateinit var comisionService: ComisionService

    @Autowired
    private lateinit var materiaService: MateriaService

    @Autowired
    private lateinit var dataService: DataService

    private lateinit var alumno: Alumno
    private lateinit var formularioSolicitud: Formulario
    private lateinit var c1: Cuatrimestre
    private lateinit var comision1Algoritmos: Comision
    private lateinit var algo: Materia

    @BeforeEach
    fun setUp() {
        val formularioCrear = FormularioCrearAlumno(
                45328,
                "Nicolas",
                "Martinez",
                "nicolas.martinez@unq.edu.ar",
                42256394,
                "42256395"
        )

        alumno = alumnoService.crear(formularioCrear)
        algo = materiaService.crear("Algoritmos", "ALG-208")
        c1 = cuatrimestreService.crear(2022, Semestre.S1)
        val horarios = listOf(
                Horario(Dia.LUNES, LocalTime.of(18, 30, 0), LocalTime.of(21, 30, 0)),
                Horario(Dia.JUEVES, LocalTime.of(18, 30, 0), LocalTime.of(21, 30, 0))
        )

        val formularioComision =  FormularioComision(
                1,
                algo.codigo,
                2022,
                Semestre.S1,
                35,
                5,
                horarios
        )
        comision1Algoritmos = comisionService.crear(formularioComision)
        formularioSolicitud = formularioService.crear(CuatrimestreId(c1.anio, c1.semestre), listOf(Solicitud(comision1Algoritmos.id!!)))

    }

    @Test
    fun `Se puede crear un alumno`() {
        assertThat(alumno).isNotNull
    }

    @Test
    fun `Un alumno registra un formulario de solicitud de cupo`() {
        val alumnoDespuesDeGuardarFormulario = alumnoService.registrarFormularioDeSolicitud(alumno.legajo,formularioSolicitud)

        assertThat(alumnoDespuesDeGuardarFormulario.haSolicitado(comision1Algoritmos)).isTrue
    }

    @Test
    fun `Un alumno no puede registrar dos formularios para el mismo cuatrimestre`() {
        alumnoService.registrarFormularioDeSolicitud(alumno.legajo, formularioSolicitud)
        val exception = assertThrows<ExcepcionUNQUE> { alumnoService.registrarFormularioDeSolicitud(alumno.legajo, formularioSolicitud) }
        assertThat(exception.message).isEqualTo("Ya has solicitado materias para este cuatrimestre")
    }

    @AfterEach
    fun tearDown() {
        dataService.clearDataSet()
    }

}