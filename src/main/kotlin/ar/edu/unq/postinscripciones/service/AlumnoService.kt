package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.*
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.persistence.*
import ar.edu.unq.postinscripciones.service.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.time.LocalDateTime
import javax.persistence.Tuple
import javax.transaction.Transactional

@Service
class AlumnoService {

    @Autowired
    private lateinit var alumnoRepository: AlumnoRepository

    @Autowired
    private lateinit var materiaRepository: MateriaRepository

    @Autowired
    private lateinit var formularioRepository: FormularioRepository

    @Autowired
    private lateinit var comisionRepository: ComisionRespository

    @Autowired
    private lateinit var cuatrimestreRepository: CuatrimestreRepository

    @Autowired
    private lateinit var solicitudSobrecupoRepository: SolicitudSobrecupoRepository

    @Transactional
    fun registrarAlumnos(planillaAlumnos: List<FormularioCrearAlumno>): List<ConflictoAlumnoDTO> {
        val alumnosConflictivos: MutableList<ConflictoAlumnoDTO> = mutableListOf()

        planillaAlumnos.forEach { formulario ->
            val existeAlumno = alumnoRepository.findByDniOrLegajo(formulario.dni, formulario.legajo)
            if (existeAlumno.isPresent) {
                alumnosConflictivos.add(ConflictoAlumnoDTO(AlumnoDTO.desdeModelo(existeAlumno.get()), formulario))
            } else {
                guardarAlumno(formulario)
            }
        }
        return alumnosConflictivos.toList()
    }

    @Transactional
    fun actualizarHistoriaAcademica(alumnoDni: Int, historiaDada: List<MateriaCursadaDTO>): AlumnoDTO {
        val alumno = alumnoRepository.findById(alumnoDni).orElseThrow { ExcepcionUNQUE("No se encontro al alumno") }

        val historiaAcademica: List<MateriaCursada> = historiaDada.map {
            val materia = materiaRepository
                    .findMateriaByCodigo(it.codigoMateria).orElseThrow { ExcepcionUNQUE("No existe la materia") }
            val materiaCursada = MateriaCursada(materia)
            materiaCursada.estado = it.estado
            materiaCursada.fechaDeCarga = it.fechaDeCarga
            materiaCursada}

        alumno.actualizarHistoriaAcademica(historiaAcademica)

        return AlumnoDTO.desdeModelo(alumnoRepository.save(alumno))
    }

    @Transactional
    fun guardarSolicitudPara(
        dni: Int,
        solicitudes: List<Long>,
        cuatrimestre: Cuatrimestre = Cuatrimestre.actual(),
        fechaCarga: LocalDateTime = LocalDateTime.now()
    ): FormularioDTO {
        val cuatrimestreObtenido =
            cuatrimestreRepository.findByAnioAndSemestre(cuatrimestre.anio, cuatrimestre.semestre)
                .orElseThrow { ExcepcionUNQUE("No existe el cuatrimestre") }
        this.checkFecha(cuatrimestreObtenido.inicioInscripciones, cuatrimestreObtenido.finInscripciones, fechaCarga)
        val alumno = alumnoRepository.findById(dni).get()
        val solicitudesPorMateria = solicitudes.map { idComision ->
            val comision = comisionRepository.findById(idComision)
            SolicitudSobrecupo(comision.get())
        }
        val materiasDisponibles = this.materiasDisponibles(alumno.dni, cuatrimestreObtenido)
        this.checkPuedeCursar(alumno, solicitudesPorMateria, materiasDisponibles)
        val formulario = formularioRepository.save(Formulario(cuatrimestreObtenido, solicitudesPorMateria))

        alumno.guardarFormulario(formulario)
        alumnoRepository.save(alumno)

        return FormularioDTO.desdeModelo(formulario, alumno.dni)
    }

    @Transactional
    fun crear(formulario: FormularioCrearAlumno): Alumno {
        return this.guardarAlumno(formulario)
    }

    @Transactional
    fun todos(): List<Alumno> {
        return alumnoRepository.findAll().toList()
    }

    @Transactional
    fun obtenerFormulario(dni: Int, cuatrimestre: Cuatrimestre = Cuatrimestre.actual()): FormularioDTO {
        val cuatrimestreObtenido =
            cuatrimestreRepository.findByAnioAndSemestre(cuatrimestre.anio, cuatrimestre.semestre)
                .orElseThrow { ExcepcionUNQUE("No existe el cuatrimestre") }
        val alumno = alumnoRepository.findById(dni).orElseThrow { ExcepcionUNQUE("No existe el alumno") }
        return FormularioDTO.desdeModelo(
            alumno.obtenerFormulario(
                cuatrimestreObtenido.anio,
                cuatrimestreObtenido.semestre
            ), alumno.dni
        )
    }

    @Transactional
    fun cambiarEstado(solicitudId: Long, estado: EstadoSolicitud): SolicitudSobrecupoDTO {
        val solicitud =
            solicitudSobrecupoRepository.findById(solicitudId).orElseThrow { ExcepcionUNQUE("No existe la solicitud") }
        solicitud.cambiarEstado(estado)
        return SolicitudSobrecupoDTO.desdeModelo(solicitudSobrecupoRepository.save(solicitud))
    }

    @Transactional
    fun cambiarEstadoFormularios() {
        val cuatrimestreObtenido = Cuatrimestre.actual()
        val alumnos = alumnoRepository.findAll()
        alumnos.forEach {
            val formulario = it.obtenerFormulario(cuatrimestreObtenido.anio, cuatrimestreObtenido.semestre)
            formulario.cambiarEstado()
        }
    }

    @Transactional
    fun materiasDisponibles(dni: Int, cuatrimestre: Cuatrimestre = Cuatrimestre.actual()): List<MateriaComision> {
        val cuatrimestreObtenido =
            cuatrimestreRepository.findByAnioAndSemestre(cuatrimestre.anio, cuatrimestre.semestre)
                .orElseThrow { ExcepcionUNQUE("No existe el cuatrimestre") }
        val alumno =
            alumnoRepository.findById(dni).orElseThrow { ExcepcionUNQUE("No existe el alumno") }
        val materiasDisponibles = materiaRepository.findMateriasDisponibles(
            alumno.materiasAprobadas(),
            alumno.carrera,
            cuatrimestreObtenido.anio,
            cuatrimestreObtenido.semestre
        )

        return this.mapToMateriaComision(materiasDisponibles)
    }

    @Transactional
    fun obtenerResumenAlumno(dni: Int): ResumenAlumno {
        val cuatrimestreObtenido = Cuatrimestre.actual()
        val alumno = alumnoRepository.findById(dni).orElseThrow { ExcepcionUNQUE("El Alumno no existe") }
        val materiasCursadas = alumnoRepository.findResumenHistoriaAcademica(dni)
                .map {
                    val materia = materiaRepository.findMateriaByCodigo(it.get(0) as String)
                            .orElseThrow{ ExcepcionUNQUE("Materia no encontrada") }
                    val fecha = (it.get(2) as java.sql.Date).toLocalDate()
                    val estado = EstadoMateria.desdeString(it.get(1) as String)
                    val intentos = (it.get(3) as BigInteger).toInt()

                    MateriaCursadaResumenDTO(materia.nombre, materia.codigo, estado, fecha, intentos)
                }

        return ResumenAlumno(
            alumno.nombre,
            alumno.dni,
            FormularioDTO.desdeModelo(
                alumno.obtenerFormulario(
                    cuatrimestreObtenido.anio,
                    cuatrimestreObtenido.semestre
                ), alumno.dni
            ),
            materiasCursadas
        )
    }

    @Transactional
    fun alumnosQueSolicitaron(idComision: Long): List<AlumnoSolicitaComision> {
        comisionRepository.findById(idComision).orElseThrow { ExcepcionUNQUE("No existe la comision") }
        val alumnos = alumnoRepository.findBySolicitaComisionIdOrderByCantidadAprobadas(idComision)

        return alumnos.map { AlumnoSolicitaComision.desdeTupla(it) }
    }

    private fun guardarAlumno(formulario: FormularioCrearAlumno): Alumno {
        val historiaAcademica = formulario.historiaAcademica.map {
            val materia = materiaRepository
                .findMateriaByCodigo(it.codigoMateria).orElseThrow { ExcepcionUNQUE("No existe la materia") }
            val materiaCursada = MateriaCursada(materia)
            materiaCursada.estado = it.estado
            materiaCursada.fechaDeCarga = it.fechaDeCarga
            materiaCursada
        }
        val alumno = Alumno(
            formulario.dni,
            formulario.nombre,
            formulario.apellido,
            formulario.correo,
            formulario.legajo,
            formulario.contrasenia,
            formulario.carrera,
        )

        historiaAcademica.forEach { alumno.cargarHistoriaAcademica(it) }

        return alumnoRepository.save(alumno)
    }

    private fun mapToMateriaComision(materiasDisponibles: List<Tuple>): List<MateriaComision> {
        val materias = mutableListOf<MateriaComision>()
        materiasDisponibles.map {
            val materiaActual = materias.find { mat -> mat.codigo == (it.get(0) as String) }
            materiaActual?.comisiones?.add(ComisionParaAlumno.desdeModelo(it.get(2) as Comision))
                ?: materias.add(
                    MateriaComision(
                        it.get(0) as String,
                        it.get(1) as String,
                        mutableListOf(ComisionParaAlumno.desdeModelo(it.get(2) as Comision))
                    )
                )
        }
        return materias
    }

    private fun checkFecha(
        inicioInscripciones: LocalDateTime,
        finInscripciones: LocalDateTime,
        fechaCargaFormulario: LocalDateTime
    ) {
        if (inicioInscripciones > fechaCargaFormulario) {
            throw ExcepcionUNQUE("El periodo para enviar solicitudes de sobrecupos no ha empezado.")
        }
        if (finInscripciones < fechaCargaFormulario) {
            throw ExcepcionUNQUE("El periodo para enviar solicitudes de sobrecupos ya ha pasado.")
        }
    }

    private fun checkPuedeCursar(alumno : Alumno, solicitudesPorMateria: List<SolicitudSobrecupo>, materiasDisponibles: List<MateriaComision>) {
        if (!alumno.puedeCursar( solicitudesPorMateria.map { it.comision.materia }, materiasDisponibles.map { it.codigo })) throw ExcepcionUNQUE("El alumno no puede cursar las materias solicitadas")
    }


}
