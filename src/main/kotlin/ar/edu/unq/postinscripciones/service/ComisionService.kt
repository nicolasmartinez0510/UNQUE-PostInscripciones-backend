package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Horario
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.model.exception.MateriaNoEncontradaExcepcion
import ar.edu.unq.postinscripciones.persistence.ComisionRespository
import ar.edu.unq.postinscripciones.persistence.CuatrimestreRepository
import ar.edu.unq.postinscripciones.persistence.MateriaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ComisionService {

    @Autowired
    private lateinit var comisionRespository: ComisionRespository

    @Autowired
    private lateinit var materiaRepository: MateriaRepository

    @Autowired
    private lateinit var cuatrimestreRepository: CuatrimestreRepository

    @Transactional
    fun crear(formularioComision: FormularioComision): Comision {
        val materia = materiaRepository.findMateriaByCodigo(formularioComision.codigoMateria)
            .orElseThrow { MateriaNoEncontradaExcepcion() }
        val cuatrimestre =
            cuatrimestreRepository.findByAnioAndSemestre(formularioComision.anio, formularioComision.semestre).get()
        return comisionRespository.save(
            Comision(
                materia,
                formularioComision.numero,
                cuatrimestre,
                formularioComision.horarios,
                formularioComision.cuposTotales,
                0,
                formularioComision.sobreCuposTotales
            )
        )
    }

    @Transactional
    fun obtener(id: Long): Comision {
        return comisionRespository.findById(id).orElseThrow { ExcepcionUNQUE("No se encuentra la comision") }
    }

    @Transactional
    fun obtenerComisionesMateria(codigoMateria: String): List<Comision> {
        val materia = materiaRepository.findMateriaByCodigo(codigoMateria)
            .orElseThrow { ExcepcionUNQUE("No se encuentra la materia") }
        val comisiones = comisionRespository.findAllByMateria(materia).get()
        comisiones.forEach { comision -> comision.horarios.size }
        return comisiones
    }
}

data class FormularioComision(
    val numero: Int,
    val codigoMateria: String,
    val anio: Int,
    val semestre: Semestre,
    val cuposTotales: Int,
    val sobreCuposTotales: Int,
    val horarios: List<Horario>
)