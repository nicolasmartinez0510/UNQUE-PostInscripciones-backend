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
        return guardarComision(formularioComision)
    }

    @Transactional
    fun obtener(id: Long): Comision {
        return comisionRespository.findById(id).orElseThrow { ExcepcionUNQUE("No se encuentra la comision") }
    }

    @Transactional
    fun obtenerComisionesMateria(codigoMateria: String): List<Comision> {
        val materia = materiaRepository.findById(codigoMateria)
            .orElseThrow { ExcepcionUNQUE("No se encuentra la materia") }
        val comisiones = comisionRespository.findAllByMateria(materia)
        comisiones.forEach { comision -> comision.horarios.size }
        return comisiones
    }

    @Transactional
    fun guardarComisiones(anio: Int, semestre: Semestre, comisionesACrear: List<ComisionACrear>) {
        val cuatrimestre = cuatrimestreRepository.findByAnioAndSemestre(anio, semestre).get()

        comisionesACrear.forEach { comisionACrear ->
            val materia = materiaRepository.findMateriaByCodigo(comisionACrear.codigoMateria)
                .orElseThrow { MateriaNoEncontradaExcepcion() }
            comisionRespository.save(
                Comision(
                    materia,
                    comisionACrear.numeroComision,
                    cuatrimestre,
                    comisionACrear.horarios.map { horarioDTO ->
                        Horario(
                            horarioDTO.dia,
                            horarioDTO.inicio,
                            horarioDTO.fin
                        )
                    },
                    comisionACrear.cuposTotales,
                    comisionACrear.cuposOcupados,
                    comisionACrear.sobrecuposTotales
                )
            )
        }
    }

    private fun guardarComision(formularioComision: FormularioComision): Comision {
        val materia = materiaRepository.findById(formularioComision.codigoMateria)
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
    fun ofertaDelCuatrimestre(idCuatrimestre: Long): List<ComisionDTO> {
        return comisionRespository.findByCuatrimestreAnioAndCuatrimestreSemestre(2022, Semestre.S1).map { ComisionDTO.desdeModelo(it) }
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

data class ComisionDTO(
    val id: Long,
    val numero: Int,
    val materia: String,
    val cuposTotales: Int,
    val sobreCuposTotales: Int,
    val cuposDisponibles: Int
) {
    companion object {
        fun desdeModelo(comision: Comision): ComisionDTO {
            return ComisionDTO(
                comision.id!!,
                comision.numero,
                comision.materia.nombre,
                comision.cuposTotales,
                comision.sobrecuposTotales,
                comision.cuposDisponibles()
            )
        }
    }
}