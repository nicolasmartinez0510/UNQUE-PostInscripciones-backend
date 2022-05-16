package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Carrera
import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Dia
import ar.edu.unq.postinscripciones.model.comision.Modalidad
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.resources.DataService
import ar.edu.unq.postinscripciones.service.dto.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
internal class ComisionServiceTest {

    @Autowired
    private lateinit var comisionService: ComisionService

    @Autowired
    private lateinit var cuatrimestreService: CuatrimestreService

    @Autowired
    private lateinit var materiaService: MateriaService

    @Autowired
    private lateinit var alumnoService: AlumnoService

    @Autowired
    private lateinit var dataService: DataService

    private lateinit var bdd: Materia
    private lateinit var c1: Cuatrimestre
    private lateinit var horarios: List<HorarioDTO>
    private lateinit var comision: Comision
    private lateinit var comision2: Comision
    private lateinit var comision3: Comision

    @BeforeEach
    fun setUp() {
        val alumno = alumnoService.crear(FormularioCrearAlumno(123312, "", "", "", 1234, "", Carrera.LICENCIATURA))
        val alumno2 = alumnoService.crear(FormularioCrearAlumno(1233123, "", "", "", 12345, "", Carrera.LICENCIATURA))

        bdd = materiaService.crear("Base de datos", "BBD-208")
        val formularioCuatrimestre = FormularioCuatrimestre(2022, Semestre.S1)
        c1 = cuatrimestreService.crear(formularioCuatrimestre)

        horarios = listOf(
            HorarioDTO(Dia.LUNES, "18:30", "21:30"),
            HorarioDTO(Dia.JUEVES, "18:30", "21:30")
        )

        val formulario = FormularioComision(
            1,
            bdd.codigo,
            2022,
            Semestre.S1,
            35,
            5,
            horarios,
            Modalidad.PRESENCIAL
        )
        comision = comisionService.crear(formulario)
        horarios = listOf(
            HorarioDTO(Dia.LUNES, "18:30", "21:30"),
            HorarioDTO(Dia.JUEVES, "18:30", "21:30")
        )
        val formulario2 = FormularioComision(
            2,
            bdd.codigo,
            2022,
            Semestre.S1,
            35,
            5,
            horarios,
            Modalidad.PRESENCIAL
        )
        val formulario3 = FormularioComision(
            2,
            bdd.codigo,
            2022,
            Semestre.S1,
            35,
            5,
            horarios,
            Modalidad.PRESENCIAL
        )
        comision2 = comisionService.crear(formulario2)
        comision3 = comisionService.crear(formulario3)
        alumnoService.guardarSolicitudPara(alumno.dni,c1.id!!, listOf(comision.id!!, comision2.id!!))
        alumnoService.guardarSolicitudPara(alumno2.dni,c1.id!!, listOf(comision2.id!!))

    }

    @Test
    fun `Se puede crear una comision`() {
        assertThat(comision).isNotNull
    }

    @Test
    fun `Se puede pedir todas las comisiones de una materia`() {
        assertThat(comisionService.obtenerComisionesMateria(bdd.codigo).first()).usingRecursiveComparison()
            .isEqualTo(ComisionDTO.desdeModelo(comision))
    }

    @Test
    fun `Se puede obtener una comision especifica`() {
        val comisionObtenida = comisionService.obtener(comision.id!!)

        assertThat(comisionObtenida).usingRecursiveComparison().isEqualTo(ComisionDTO.desdeModelo(comision))
    }

    @Test
    fun `No se puede obtener una comision que no existe`() {
        val exception = assertThrows<ExcepcionUNQUE> { comisionService.obtener(99999) }

        assertThat(exception.message).isEqualTo("No se encuentra la comision")
    }

    @Test
    fun `No se pueden obtener las comisiones de una materia que no existe`() {
        val exception = assertThrows<ExcepcionUNQUE> { comisionService.obtenerComisionesMateria("AA-209") }

        assertThat(exception.message).isEqualTo("No se encuentra la materia")
    }

    @Test
    fun `Obtener comisiones ordenadas por cantidad de solicitudes`() {
        val comisionesObtenidas = comisionService.comisionesPorSolicitudes(c1)
        assertThat(comisionesObtenidas.maxOf { it.cantSolicitudes }).isEqualTo(comisionesObtenidas.first().cantSolicitudes)
        assertThat(comisionesObtenidas.minOf { it.cantSolicitudes }).isEqualTo(comisionesObtenidas.last().cantSolicitudes)
        assertThat(comisionesObtenidas.first().comision).usingRecursiveComparison().isEqualTo(ComisionDTO.desdeModelo(comision2))
        assertThat(comisionesObtenidas.first().cantSolicitudes).usingRecursiveComparison().isEqualTo(2)
    }


    @AfterEach
    fun tearDown() {
        dataService.clearDataSet()
    }

}