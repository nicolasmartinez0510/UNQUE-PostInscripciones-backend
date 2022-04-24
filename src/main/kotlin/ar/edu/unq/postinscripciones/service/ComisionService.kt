package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Horario
import ar.edu.unq.postinscripciones.model.cuatrimestre.CuatrimestreId
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.persistence.ComisionRespository
import ar.edu.unq.postinscripciones.persistence.CuatrimestreRepository
import ar.edu.unq.postinscripciones.persistence.HorarioRepository
import ar.edu.unq.postinscripciones.persistence.MateriaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Year
import javax.transaction.Transactional

@Service
class ComisionService {

    @Autowired
    private lateinit var comisionRespository: ComisionRespository

    @Autowired
    private lateinit var materiaRepository: MateriaRepository

    @Autowired
    private lateinit var cuatrimestreRepository: CuatrimestreRepository

    @Autowired
    private lateinit var horarioRepository: HorarioRepository

    @Transactional
    fun crear(formularioComision: FormularioComision): Comision {
        val materia = materiaRepository.findMateriaByCodigo(formularioComision.codigoMateria).get()
        val cuatrimestre = cuatrimestreRepository.findById(CuatrimestreId(formularioComision.anio,formularioComision.semestre)).get()
        val horarios = horarioRepository.saveAll(formularioComision.horarios).toList()
        return comisionRespository.save(Comision(materia, formularioComision.numero, cuatrimestre, horarios, formularioComision.cuposTotales, 0, formularioComision.sobreCuposTotales))
    }

    @Transactional
    fun cuposDisponiblesComision(id: Long): Int {
        return comisionRespository.findById(id).get().cuposDisponibles()
    }

    @Transactional
    fun obtenerComisionesMateria(codigoMateria: String): List<Comision> {
        val materia = materiaRepository.findMateriaByCodigo(codigoMateria).get()
        val comisiones = comisionRespository.findAllByMateria(materia).get()
        comisiones.forEach { comision -> comision.horarios.size }
        return comisiones
    }
}

data class FormularioComision(
        val numero: Int,
        val codigoMateria: String,
        val anio: Year,
        val semestre: Semestre,
        val cuposTotales: Int,
        val sobreCuposTotales: Int,
        val horarios: List<Horario>
)