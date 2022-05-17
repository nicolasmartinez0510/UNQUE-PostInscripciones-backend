package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.*
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.persistence.*
import ar.edu.unq.postinscripciones.service.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
    fun guardarSolicitudPara(dni: Int, idCuatrimestre: Long, solicitudes: List<Long>): FormularioDTO {
        val alumno = alumnoRepository.findById(dni).get()
        val cuatrimestre = cuatrimestreService.findById(idCuatrimestre).get()
        val solicitudesPorMateria = solicitudes.map { idComision ->
            val comision = comisionRepository.findById(idComision)
            SolicitudSobrecupo(comision.get())
        }
        val formulario = formularioRepository.save(Formulario(cuatrimestre, solicitudesPorMateria))

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
    fun obtenerFormulario(anio: Int, semestre: Semestre, dni: Int): FormularioDTO {
        val alumno = alumnoRepository.findById(dni).orElseThrow { ExcepcionUNQUE("No existe el alumno") }
        return FormularioDTO.desdeModelo(alumno.obtenerFormulario(anio, semestre), alumno.dni)
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

    @Transactional
    fun materiasDisponibles(dni: Int, anio: Int, semestre: Semestre): List<MateriaComision> {
        val cuatrimestre = cuatrimestreService.findByAnioAndSemestre(anio, semestre).orElseThrow { ExcepcionUNQUE("No existe el cuatrimestre") }
        val alumno =
            alumnoRepository.findByDni(dni).orElseThrow { ExcepcionUNQUE("No existe el alumno") }
        val materiasDisponibles = materiaRepository.findMateriasDisponibles(alumno.materiasAprobadas(), alumno.carrera!!, cuatrimestre.anio, cuatrimestre.semestre)

        return this.mapToMateriaComision(materiasDisponibles)
    }

    private fun mapToMateriaComision(materiasDisponibles: List<Tuple>): List<MateriaComision> {
        val materias = mutableListOf<MateriaComision>()
        materiasDisponibles.map {
            val materiaActual = materias.find{mat -> mat.codigo == (it.get(0) as String)}
            materiaActual?.comisiones?.add(ComisionDTO.desdeModelo(it.get(2) as Comision))
                ?: materias.add(
                    MateriaComision(
                        it.get(0) as String,
                        it.get(1) as String,
                        mutableListOf(ComisionDTO.desdeModelo(it.get(2) as Comision))
                    )
                )
        }
        return materias
    }
}

