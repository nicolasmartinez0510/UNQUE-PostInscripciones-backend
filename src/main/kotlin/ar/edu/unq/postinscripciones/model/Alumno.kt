package ar.edu.unq.postinscripciones.model

import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import javax.persistence.*

@Entity
class Alumno(
    @Id
    val dni: Int = 1234,
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    @Column(unique = true)
    val legajo: Int = 4,
    val contrasenia: String = "",
    val carrera: Carrera? = null
) {
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    private val formularios: MutableList<Formulario> = mutableListOf()

    fun guardarFormulario(formulario: Formulario) {
        carrera ?: throw ExcepcionUNQUE("Un alumno sin carrera no puede solicitar cupos")
        chequearSiExiste(formulario)
        formularios.add(formulario)
    }


    fun obtenerFormulario(anio: Int, semestre: Semestre): Formulario {
        val formulario = formularios.find { it.cuatrimestre.esElCuatrimestre(anio, semestre) }
        return formulario ?: throw ExcepcionUNQUE("No se encontró ningun formulario para el cuatrimestre dado")
    }

    fun haSolicitado(unaComision: Comision): Boolean {
        return formularios
            .filter { formulario -> formulario.cuatrimestre.esElCuatrimestre(unaComision.cuatrimestre) }
            .any { formulario ->
                formulario
                    .solicitudes
                    .any { solicitudSobrecupo -> solicitudSobrecupo.comision.esLaComision(unaComision) }
            }
    }

    fun llenoElFormularioDelCuatrimestre(cuatrimestre: Cuatrimestre): Boolean {
        return formularios.any { formulario -> formulario.cuatrimestre.esElCuatrimestre(cuatrimestre) }
    }

    private fun chequearSiExiste(formulario: Formulario) {
        if (llenoElFormularioDelCuatrimestre(formulario.cuatrimestre)) {
            throw ExcepcionUNQUE("Ya has solicitado materias para este cuatrimestre")
        }
    }
}