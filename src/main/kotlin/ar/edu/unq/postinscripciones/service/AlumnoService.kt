package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Alumno
import ar.edu.unq.postinscripciones.model.EstadoSolicitud
import ar.edu.unq.postinscripciones.model.Formulario
import ar.edu.unq.postinscripciones.model.SolicitudSobrecupo
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.persistence.*
import ar.edu.unq.postinscripciones.service.dto.FormularioDTO
import ar.edu.unq.postinscripciones.service.dto.SolicitudSobrecupoDTO
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
    fun registrarAlumnos(planillaAlumnos: List<FormularioCrearAlumno>) {
        planillaAlumnos.forEach { formulario ->
            alumnoRepository.findById(formulario.legajo)
                .ifPresent { throw ExcepcionUNQUE("Ya existe el alumno con el legajo ${formulario.legajo}. Intente nuevamente") }
            guardarAlumno(formulario)
        }
    }

    @Transactional
    fun guardarSolicitudPara(legajo: Int, idCuatrimestre: Long, solicitudes: List<Long>): FormularioDTO {
        val alumno = alumnoRepository.findById(legajo).get()
        val cuatrimestre = cuatrimestreService.findById(idCuatrimestre).get()
        val solicitudesPorMateria = solicitudes.map { idComision ->
            val comision = comisionRepository.findById(idComision)
            SolicitudSobrecupo(comision.get())
        }
        val formulario = formularioRepository.save(Formulario(cuatrimestre, solicitudesPorMateria))

        alumno.guardarFormulario(formulario)
        alumnoRepository.save(alumno)

        return FormularioDTO.desdeModelo(formulario, alumno.legajo)
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
    fun obtenerFormulario(anio: Int, semestre: Semestre, legajo: Int): FormularioDTO {
        val alumno = alumnoRepository.findById(legajo).orElseThrow { ExcepcionUNQUE("No existe el alumno") }
        return FormularioDTO.desdeModelo(alumno.obtenerFormulario(anio, semestre), alumno.legajo)
    }

    @Transactional
    fun cambiarEstado(solicitudId: Long, estado: EstadoSolicitud): SolicitudSobrecupoDTO {
        val solicitud = solicitudSobrecupoRepository.findById(solicitudId).orElseThrow{ ExcepcionUNQUE("No existe la solicitud") }
        solicitud.cambiarEstado(estado)
        return SolicitudSobrecupoDTO.desdeModelo(solicitudSobrecupoRepository.save(solicitud))
    }

    private fun guardarAlumno(formulario: FormularioCrearAlumno): Alumno {
        return alumnoRepository.save(
            Alumno(
                formulario.legajo,
                formulario.nombre,
                formulario.apellido,
                formulario.correo,
                formulario.dni,
                formulario.contrasenia
            )
        )
    }
}

data class FormularioCrearAlumno(
    val legajo: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val dni: Int,
    val contrasenia: String
)