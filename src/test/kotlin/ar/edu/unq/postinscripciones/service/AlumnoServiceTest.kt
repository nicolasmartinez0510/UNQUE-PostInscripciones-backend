package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.*
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
import java.time.LocalDate
import java.time.LocalDateTime

@IntegrationTest
internal class AlumnoServiceTest {

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
    private lateinit var fede: Alumno
    private lateinit var cuatrimestre: Cuatrimestre
    private lateinit var comision1Algoritmos: Comision
    private lateinit var algo: Materia
    private lateinit var funcional: Materia

    @BeforeEach
    fun setUp() {
        val nicoFormularioCrear = FormularioCrearAlumno(
            45328,
            "Nicolas",
            "Martinez",
            "nicolas.martinez@unq.edu.ar",
            42256394,
            "42256395",
            Carrera.TPI,
            listOf()
        )

        val fedeFormularioCrear = FormularioCrearAlumno(
            45329,
            "Fede",
            "Sandoval",
            "fede.sando@unq.edu.ar",
            11223344,
            "1234",
            Carrera.TPI,
            listOf()
        )

        alumno = alumnoService.crear(nicoFormularioCrear)
        fede = alumnoService.crear(fedeFormularioCrear)
        algo = materiaService.crear("Algoritmos", "ALG-208", mutableListOf(), Carrera.SIMULTANEIDAD)
        funcional = materiaService.crear("Funcional", "FUN-205", mutableListOf(), Carrera.SIMULTANEIDAD)
        val formularioCuatrimestre = FormularioCuatrimestre(2022, Semestre.S1)
        cuatrimestre = cuatrimestreService.crear(formularioCuatrimestre)
        val horarios = listOf(
            HorarioDTO(Dia.LUNES, "18:30", "21:30"),
            HorarioDTO(Dia.JUEVES, "18:30", "21:30")
        )

        val formularioComision = FormularioComision(
            1,
            algo.codigo,
            2022,
            Semestre.S1,
            35,
            5,
            horarios,
            Modalidad.PRESENCIAL
        )
        comision1Algoritmos = comisionService.crear(formularioComision)
    }

    @Test
    fun `Se puede crear un alumno`() {
        assertThat(alumno).isNotNull
    }

    @Test
    fun `Un alumno registra un formulario de solicitud de cupo`() {
        val formulario =
            alumnoService.guardarSolicitudPara(
                alumno.dni,
                cuatrimestre.id!!,
                listOf(comision1Algoritmos.id!!)
            )
        val comisionesDeSolicitudes = formulario.solicitudes.map { it.comisionDTO }

        assertThat(comisionesDeSolicitudes).contains(ComisionDTO.desdeModelo(comision1Algoritmos))
    }

    @Test
    fun `Un alumno no puede registrar dos formularios para el mismo cuatrimestre`() {
        alumnoService.guardarSolicitudPara(
            alumno.dni,
            cuatrimestre.id!!,
            listOf(comision1Algoritmos.id!!)
        )
        val exception = assertThrows<ExcepcionUNQUE> {
            alumnoService.guardarSolicitudPara(
                alumno.dni,
                cuatrimestre.id!!,
                listOf()
            )
        }
        assertThat(exception.message).isEqualTo("Ya has solicitado materias para este cuatrimestre")
    }

    @Test
    fun `Se puede obtener el formulario`() {
        val formularioDTO =
            alumnoService.guardarSolicitudPara(
                alumno.dni,
                cuatrimestre.id!!,
                listOf(comision1Algoritmos.id!!)
            )
        val formularioPersistido = alumnoService.obtenerFormulario(cuatrimestre.anio, cuatrimestre.semestre, alumno.dni)

        assertThat(formularioDTO).usingRecursiveComparison().isEqualTo(formularioPersistido)
    }

    @Test
    fun `Se puede aprobar una solicitud de sobrecupo`() {
        val formulario =
            alumnoService.guardarSolicitudPara(
                alumno.dni,
                cuatrimestre.id!!,
                listOf(comision1Algoritmos.id!!)
            )
        val solicitudPendiente = formulario.solicitudes.first()
        val solicitudAprobada = alumnoService.cambiarEstado(solicitudPendiente.id, EstadoSolicitud.APROBADO)

        assertThat(solicitudAprobada.estado).isEqualTo(EstadoSolicitud.APROBADO)
        assertThat(solicitudAprobada).usingRecursiveComparison().ignoringFields("estado").isEqualTo(solicitudPendiente)
    }

    @Test
    fun `Se puede rechazar una solicitud de sobrecupo`() {
        val formulario =
            alumnoService.guardarSolicitudPara(
                alumno.dni,
                cuatrimestre.id!!,
                listOf(comision1Algoritmos.id!!)
            )
        val solicitudPendiente = formulario.solicitudes.first()
        val solicitudRechazada = alumnoService.cambiarEstado(solicitudPendiente.id, EstadoSolicitud.RECHAZADO)

        assertThat(solicitudRechazada.estado).isEqualTo(EstadoSolicitud.RECHAZADO)
    }

    @Test
    fun `Se pueden cerrar todos los formularios del cuatrimestre corriente`() {
        val formularioAntesDeCerrar =
            alumnoService.guardarSolicitudPara(
                alumno.dni,
                cuatrimestre.id!!,
                listOf(comision1Algoritmos.id!!)
            )
        val formulario2AntesDeCerrar =
            alumnoService.guardarSolicitudPara(
                fede.dni,
                cuatrimestre.id!!,
                listOf(comision1Algoritmos.id!!)
            )

        alumnoService.cambiarEstadoFormularios(cuatrimestre.anio, cuatrimestre.semestre)
        val formularioDespuesDeCerrar =
            alumnoService.obtenerFormulario(cuatrimestre.anio, cuatrimestre.semestre, alumno.dni)
        val formulario2DespuesDeCerrar =
            alumnoService.obtenerFormulario(cuatrimestre.anio, cuatrimestre.semestre, fede.dni)

        assertThat(listOf(formularioAntesDeCerrar, formulario2AntesDeCerrar).map { it.estado })
            .usingRecursiveComparison()
            .isEqualTo(listOf(EstadoFormulario.ABIERTO, EstadoFormulario.ABIERTO))

        assertThat(listOf(formularioDespuesDeCerrar, formulario2DespuesDeCerrar).map { it.estado })
            .usingRecursiveComparison()
            .isEqualTo(listOf(EstadoFormulario.CERRADO, EstadoFormulario.CERRADO))
    }

    @Test
    fun `Se puede cargar la historia academica de un alumno`() {
        val materiaCursada = MateriaCursadaDTO(funcional.codigo, EstadoMateria.APROBADO, LocalDate.of(2021, 12, 20))
        val formularioAlumno = FormularioCrearAlumno(
            1234567,
            "Pepe",
            "Sanchez",
            "pepe.sanchez@unq.edu.ar",
            44556,
            "1234",
            Carrera.TPI,
            listOf(materiaCursada)
        )

        val alumno = alumnoService.crear(formularioAlumno)

        assertThat(alumno.historiaAcademica).isNotEmpty
        assertThat(alumno.historiaAcademica.first().materia.codigo).isEqualTo(materiaCursada.codigoMateria)
    }

    @Test
    fun `Se puede obtener las materias disponibles de un alumno`() {
        val materiasdisponibles =
            alumnoService.materiasDisponibles(alumno.dni, cuatrimestre.anio, cuatrimestre.semestre)
        assertThat(materiasdisponibles).isNotEmpty
        assertThat(materiasdisponibles.first().codigo).isEqualTo(algo.codigo)
    }

    @Test
    fun `un alumno tiene disponible materias solo de su carrera`() {
        val logica = materiaService.crear("Lógica y Programacion", "LOG-209", mutableListOf(), Carrera.LICENCIATURA)
        val materiasdisponibles =
            alumnoService.materiasDisponibles(alumno.dni, cuatrimestre.anio, cuatrimestre.semestre)
        assertThat(materiasdisponibles.map { it.codigo }).doesNotContain(logica.codigo)
    }

    @Test
    fun `un alumno no tiene disponible materias de las cuales no cumple los requisitos`() {
        val logica = materiaService.crear("Lógica y Programacion", "LOG-209", mutableListOf("ALG-208"), Carrera.TPI)
        val materiasdisponibles =
            alumnoService.materiasDisponibles(alumno.dni, cuatrimestre.anio, cuatrimestre.semestre)
        assertThat(materiasdisponibles).hasSize(1)
        assertThat(materiasdisponibles.map { it.codigo }).contains(algo.codigo).doesNotContain(logica.codigo)
    }

    @Test
    fun `un alumno tiene disponible materias de las cuales cumple los requisitos`() {
        val materiaCursada = MateriaCursadaDTO(algo.codigo, EstadoMateria.APROBADO, LocalDate.of(2021, 12, 20))
        val formularioAlumno = FormularioCrearAlumno(
            123456712,
            "Pepe",
            "Sanchez",
            "pepe.sanchez@unq.edu.ar",
            4455611,
            "1234",
            Carrera.TPI,
            listOf(materiaCursada)
        )
        val nacho = alumnoService.crear(formularioAlumno)
        val logica = materiaService.crear("Lógica y Programacion", "LOG-209", mutableListOf(algo.codigo), Carrera.TPI)
        val formularioComision = FormularioComision(
            1,
            logica.codigo,
            cuatrimestre.anio,
            cuatrimestre.semestre,
            35,
            5,
            listOf(
                HorarioDTO(Dia.LUNES, "18:00", "20:00"),
                HorarioDTO(Dia.JUEVES, "09:00", "11:00")
            ),
            Modalidad.PRESENCIAL
        )
        val comisionLogica = comisionService.crear(formularioComision)
        val materiasdisponibles = alumnoService.materiasDisponibles(nacho.dni, cuatrimestre.anio, cuatrimestre.semestre)
        assertThat(materiasdisponibles).hasSize(1)
        assertThat(materiasdisponibles.map { it.codigo }).contains(logica.codigo)
        assertThat(materiasdisponibles.first().comisiones).allMatch { it == ComisionParaAlumno.desdeModelo(comisionLogica)  }
    }

    @Test
    fun `un alumno no tiene disponible materias que ya aprobo`() {
        val materiaCursada = MateriaCursadaDTO(algo.codigo, EstadoMateria.APROBADO, LocalDate.of(2021, 12, 20))
        val formularioAlumno = FormularioCrearAlumno(
            123456712,
            "Pepe",
            "Sanchez",
            "pepe.sanchez@unq.edu.ar",
            4455611,
            "1234",
            Carrera.TPI,
            listOf(materiaCursada)
        )
        val nacho = alumnoService.crear(formularioAlumno)
        val materiasdisponibles = alumnoService.materiasDisponibles(nacho.dni, cuatrimestre.anio, cuatrimestre.semestre)
        assertThat(materiasdisponibles.map { it.codigo }).doesNotContain(algo.codigo)
    }

    @Test
    fun `se levanta una excepcion al enviar un formulario pasada la fecha de fin aceptada por el cuatrimestre`() {
        comisionService.actualizarOfertaAcademica(listOf(), LocalDateTime.now(), LocalDateTime.now().plusDays(3))

        val excepcion = assertThrows<ExcepcionUNQUE> {
            alumnoService.guardarSolicitudPara(
                alumno.dni,
                cuatrimestre.id!!,
                listOf(comision1Algoritmos.id!!),
                LocalDateTime.now().plusDays(5)
            )
        }
        assertThat(excepcion.message).isEqualTo("El periodo para enviar solicitudes de sobrecupos ya ha pasado.")
    }

    @Test
    fun `se levanta una excepcion al enviar un formulario antes de la fecha de inicio aceptada por el cuatrimestre`() {
        comisionService.actualizarOfertaAcademica(listOf(), LocalDateTime.now(), LocalDateTime.now().plusDays(3))

        val excepcion = assertThrows<ExcepcionUNQUE> {
            alumnoService.guardarSolicitudPara(
                alumno.dni,
                cuatrimestre.id!!,
                listOf(comision1Algoritmos.id!!),
                LocalDateTime.now().minusDays(5)
            )
        }
        assertThat(excepcion.message).isEqualTo("El periodo para enviar solicitudes de sobrecupos no ha empezado.")
    }

//    @Test
//    fun `Se puede cambiar el estado de una materia cursada`() {
//        val materiaCursada = MateriaCursadaDTO(funcional.codigo, EstadoMateria.APROBADO, LocalDate.of(2021, 12, 20))
//        val formularioAlumno = FormularioCrearAlumno(
//                1234567,
//                "Pepe",
//                "Sanchez",
//                "pepe.sanchez@unq.edu.ar",
//                44556,
//                "1234",
//                Carrera.TPI,
//                listOf(materiaCursada)
//        )
//
//        alumnoService.crear(formularioAlumno);
//
//        val materiaCursadaNuevoEstado = alumnoService
//                .cambiarEstadoMateriaCursada(funcional.codigo, EstadoMateria.APROBADO)
//
//        assertThat(materiaCursadaNuevoEstado.estado).isEqualTo(EstadoMateria.APROBADO)
//    }

    @AfterEach
    fun tearDown() {
        dataService.clearDataSet()
    }

}