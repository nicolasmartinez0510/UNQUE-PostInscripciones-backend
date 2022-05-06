package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.model.exception.MateriaNoEncontradaExcepcion
import ar.edu.unq.postinscripciones.persistence.ComisionRespository
import ar.edu.unq.postinscripciones.persistence.CuatrimestreRepository
import ar.edu.unq.postinscripciones.persistence.MateriaRepository
import ar.edu.unq.postinscripciones.service.dto.*
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
    fun guardarComisiones(anio: Int, semestre: Semestre, comisionesACrear: List<ComisionACrear>): List<ConflictoComision> {
        val cuatrimestre = cuatrimestreRepository.findByAnioAndSemestre(anio, semestre)
            .orElseThrow { ExcepcionUNQUE("Cuatrimestre no encontrado") }

        return guardarComisionesBuscandoConflictos(comisionesACrear, cuatrimestre)
    }

    @Transactional
    fun ofertaDelCuatrimestre(anio: Int, semestre: Semestre): List<ComisionDTO> {
        return comisionRespository.findByCuatrimestreAnioAndCuatrimestreSemestre(anio, semestre)
            .map { ComisionDTO.desdeModelo(it) }
    }

    @Transactional
    fun crear(formularioComision: FormularioComision): Comision {
        return guardarComision(formularioComision)
    }

    @Transactional
    fun obtener(id: Long): ComisionDTO {
        val comision = comisionRespository.findById(id).orElseThrow { ExcepcionUNQUE("No se encuentra la comision") }
        return ComisionDTO.desdeModelo(comision)
    }

    @Transactional
    fun obtenerComisionesMateria(codigoMateria: String): List<ComisionDTO> {
        val materia = materiaRepository.findById(codigoMateria)
            .orElseThrow { ExcepcionUNQUE("No se encuentra la materia") }
        val comisiones = comisionRespository.findAllByMateria(materia)

        return comisiones.map { ComisionDTO.desdeModelo(it) }
    }

    private fun guardarComisionesBuscandoConflictos(
        comisionesACrear: List<ComisionACrear>,
        cuatrimestre: Cuatrimestre,
    ): List<ConflictoComision> {
        val comisionesConflictivas = mutableListOf<ConflictoComision>()
        comisionesACrear.forEach { comisionACrear ->
            val materia = materiaRepository.findMateriaByCodigo(comisionACrear.codigoMateria)
                .orElseThrow { MateriaNoEncontradaExcepcion() }
            val existeComision = comisionRespository
                .findByNumeroAndMateriaAndCuatrimestre(comisionACrear.numeroComision, materia, cuatrimestre)
            if (existeComision.isPresent) {
                comisionesConflictivas.add(
                    ConflictoComision(
                        ComisionDTO.desdeModelo(existeComision.get()),
                        comisionACrear
                    )
                )
            }
            guardarComision(comisionACrear, materia, cuatrimestre)
        }
        return comisionesConflictivas
    }

    private fun guardarComision(
        comisionACrear: ComisionACrear,
        materia: Materia,
        cuatrimestre: Cuatrimestre
    ): Comision {
        return comisionRespository.save(
            Comision(
                materia,
                comisionACrear.numeroComision,
                cuatrimestre,
                comisionACrear.horarios.map { HorarioDTO.aModelo(it) },
                comisionACrear.cuposTotales,
                comisionACrear.cuposOcupados,
                comisionACrear.sobrecuposTotales
            )
        )
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
                formularioComision.horarios.map { HorarioDTO.aModelo(it) },
                formularioComision.cuposTotales,
                0,
                formularioComision.sobreCuposTotales
            )
        )
    }
}
