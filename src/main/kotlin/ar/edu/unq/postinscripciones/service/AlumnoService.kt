package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Alumno
import ar.edu.unq.postinscripciones.model.EstadoSolicitud
import ar.edu.unq.postinscripciones.model.Formulario
import ar.edu.unq.postinscripciones.model.SolicitudSobrecupo
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.persistence.*
import ar.edu.unq.postinscripciones.service.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AlumnoService {

    @Autowired
    private lateinit var alumnoRepository: AlumnoRepository

    @Autowired
    private lateinit var formularioRepository: FormularioRepository

    @Autowired
    private lateinit var comisionRepository: ComisionRespository

    @Autowired
    private lateinit var cuatrimestreService: CuatrimestreRepository

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

    private fun guardarAlumno(formulario: FormularioCrearAlumno): Alumno {
        return alumnoRepository.save(
            Alumno(
                formulario.dni,
                formulario.nombre,
                formulario.apellido,
                formulario.correo,
                formulario.legajo,
                formulario.contrasenia,
                formulario.carrera
            )
        )
    }
}

