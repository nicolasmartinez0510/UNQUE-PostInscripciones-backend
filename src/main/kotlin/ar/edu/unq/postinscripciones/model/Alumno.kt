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
    val carrera: Carrera = Carrera.SIMULTANEIDAD,
) {
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    private val formularios: MutableList<Formulario> = mutableListOf()

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    val historiaAcademica: MutableList<MateriaCursada> = mutableListOf()

    fun guardarFormulario(formulario: Formulario) {
        carrera
        chequearSiExiste(formulario)
        formularios.add(formulario)
    }

    fun cargarHistoriaAcademica(materiaCursada: MateriaCursada) {
        historiaAcademica.add(materiaCursada)
        historiaAcademica.sortByDescending { it.fechaDeCarga }
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

    fun materiasCursadasPorEstadoDeMateria(estadoMateria: EstadoMateria): List<MateriaCursada> {
        return historiaAcademica.filter { it.estado == estadoMateria }
    }

    fun materiasAprobadas(): List<Materia>{
        return materiasCursadasPorEstadoDeMateria(EstadoMateria.APROBADO).map { it.materia }
    }

    fun cantidadDeVecesQueCurso(materia: Materia): Int {
       return historiaAcademica.count { it.materia.esLaMateria(materia) }
    }

    private fun chequearSiExiste(formulario: Formulario) {
        if (llenoElFormularioDelCuatrimestre(formulario.cuatrimestre)) {
            throw ExcepcionUNQUE("Ya has solicitado materias para este cuatrimestre")
        }
    }
}