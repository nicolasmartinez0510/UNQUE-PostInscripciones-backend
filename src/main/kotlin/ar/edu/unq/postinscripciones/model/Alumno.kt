package ar.edu.unq.postinscripciones.model

import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Alumno(
    @Id
    val legajo: Int = 1234,
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val dni: Int = 4,
    val contrasenia: String = ""
) {
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    private val formularios: MutableList<Formulario> = mutableListOf()

    fun guardarFormulario(formulario: Formulario) {
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
