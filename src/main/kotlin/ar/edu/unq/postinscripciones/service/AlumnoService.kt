package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.*
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.persistence.*
import ar.edu.unq.postinscripciones.service.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
    private lateinit var cuatrimestreService: CuatrimestreRepository

    @Autowired
    private lateinit var solicitudSobrecupoRepository: SolicitudSobrecupoRepository

//    @Autowired
//    private lateinit var materiaCursadaRepository: MateriaCursadaRepository

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
    fun guardarSolicitudPara(
        dni: Int,
        solicitudes: List<Long>,
        cuatrimestre: Cuatrimestre = Cuatrimestre.actual(),
        fechaCarga: LocalDateTime = LocalDateTime.now()
    ): FormularioDTO {
        val cuatrimestreObtenido = cuatrimestreService.findByAnioAndSemestre(cuatrimestre.anio, cuatrimestre.semestre).orElseThrow { ExcepcionUNQUE("No existe el cuatrimestre") }
        this.checkFecha(cuatrimestreObtenido.inicioInscripciones, cuatrimestreObtenido.finInscripciones, fechaCarga)
        val alumno = alumnoRepository.findById(dni).get()
        val solicitudesPorMateria = solicitudes.map { idComision ->
            val comision = comisionRepository.findById(idComision)
            SolicitudSobrecupo(comision.get())
        }
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
        val cuatrimestreObtenido = cuatrimestreService.findByAnioAndSemestre(cuatrimestre.anio, cuatrimestre.semestre).orElseThrow { ExcepcionUNQUE("No existe el cuatrimestre") }
        val alumno = alumnoRepository.findById(dni).orElseThrow { ExcepcionUNQUE("No existe el alumno") }
        return FormularioDTO.desdeModelo(alumno.obtenerFormulario(cuatrimestreObtenido.anio, cuatrimestreObtenido.semestre), alumno.dni)
    }

    @Transactional
    fun cambiarEstado(solicitudId: Long, estado: EstadoSolicitud): SolicitudSobrecupoDTO {
        val solicitud =
            solicitudSobrecupoRepository.findById(solicitudId).orElseThrow { ExcepcionUNQUE("No existe la solicitud") }
        solicitud.cambiarEstado(estado)
        return SolicitudSobrecupoDTO.desdeModelo(solicitudSobrecupoRepository.save(solicitud))
    }

    @Transactional
    fun cambiarEstadoFormularios(anio: Int, semestre: Semestre) {
        val alumnos = alumnoRepository.findAll()
        alumnos.forEach {
            val formulario = it.obtenerFormulario(anio, semestre)
            formulario.cambiarEstado()
        }
    }

//    @Transactional
//    fun cambiarEstadoMateriaCursada(codigoMateria: String, estadoMateria: EstadoMateria): MateriaCursada {
//        val materia = materiaRepository.findMateriaByCodigo(codigoMateria).get()
//        val materiaCursada = materiaCursadaRepository.findByMateria(materia).get()
//
//        materiaCursada.cambiarEstado(estadoMateria)
//
//        return materiaCursadaRepository.save(materiaCursada)
//    }

    @Transactional
    fun materiasDisponibles(dni: Int, cuatrimestre: Cuatrimestre = Cuatrimestre.actual()): List<MateriaComision> {
        val cuatrimestreObtenido = cuatrimestreService.findByAnioAndSemestre(cuatrimestre.anio, cuatrimestre.semestre)
            .orElseThrow { ExcepcionUNQUE("No existe el cuatrimestre") }
        val alumno =
            alumnoRepository.findByDni(dni).orElseThrow { ExcepcionUNQUE("No existe el alumno") }
        val materiasDisponibles = materiaRepository.findMateriasDisponibles(
            alumno.materiasAprobadas(),
            alumno.carrera,
            cuatrimestreObtenido.anio,
            cuatrimestreObtenido.semestre
        )

        return this.mapToMateriaComision(materiasDisponibles)
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

}

