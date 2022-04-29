package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Alumno
import ar.edu.unq.postinscripciones.model.Formulario
import ar.edu.unq.postinscripciones.model.SolicitudSobrecupo
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.persistence.AlumnoRepository
import ar.edu.unq.postinscripciones.persistence.ComisionRespository
import ar.edu.unq.postinscripciones.persistence.CuatrimestreRepository
import ar.edu.unq.postinscripciones.persistence.FormularioRepository
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

    @Transactional
    fun registrarAlumnos(planillaAlumnos: List<FormularioCrearAlumno>) {
        planillaAlumnos.forEach { formulario ->
            alumnoRepository.findById(formulario.legajo)
                .ifPresent { throw ExcepcionUNQUE("Ya existe el alumno con el legajo ${formulario.legajo}. Intente nuevamente") }
            guardarAlumno(formulario)
        }
    }

    @Transactional
    fun guardarSolicitudPara(legajoAlumno: Int, idCuatrimestre: Long, comisionesSolicitadas: List<Long>): Alumno {
        val alumno = alumnoRepository.findById(legajoAlumno).get()
        val cuatrimestre = cuatrimestreService.findById(idCuatrimestre).get()
        val solicitudesPorMateria = comisionesSolicitadas.map { idComision ->
            val comision = comisionRepository.findById(idComision)
            SolicitudSobrecupo(comision.get())
        }
        val formulario = formularioRepository.save(Formulario(cuatrimestre, solicitudesPorMateria))

        alumno.guardarFormulario(formulario)

        return alumnoRepository.save(alumno)
    }

    @Transactional
    fun crear(formulario: FormularioCrearAlumno): Alumno {
        return this.guardarAlumno(formulario)
    }

    @Transactional
    fun todos(): List<Alumno> {
        return alumnoRepository.findAll().toList()
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