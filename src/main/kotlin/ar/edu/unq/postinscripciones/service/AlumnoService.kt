package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Alumno
import ar.edu.unq.postinscripciones.model.Formulario
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.persistence.AlumnoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AlumnoService {

    @Autowired
    private lateinit var alumnoRepository: AlumnoRepository

    @Transactional
    fun crear(formulario: FormularioCrearAlumno): Alumno {
        return this.guardarAlumno(formulario)
    }

    @Transactional
    fun registrarFormularioDeSolicitud(legajo: Int, formulario: Formulario): Alumno {
        val alumno = alumnoRepository.findById(legajo).get()
        alumno.guardarFormulario(formulario)
        return alumnoRepository.save(alumno)
    }

    @Transactional
    fun todos(): List<Alumno> {
        return alumnoRepository.findAll().toList()
    }

    @Transactional
    fun registrarAlumnos(planillaAlumnos: List<FormularioCrearAlumno>) {
        planillaAlumnos.forEach { formulario ->
            alumnoRepository.findById(formulario.legajo)
                .ifPresent { throw ExcepcionUNQUE("Ya existe el alumno con el legajo ${formulario.legajo}. Intente nuevamente") }
            guardarAlumno(formulario)
        }
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