package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Carrera
import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.model.exception.MateriaNoEncontradaExcepcion
import ar.edu.unq.postinscripciones.persistence.MateriaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class MateriaService {
    @Autowired
    private lateinit var materiaRepository: MateriaRepository


    @Transactional
    fun crear(nombre: String, codigo: String, correlativas : List<String>, carrera: Carrera): Materia {
        val existeConNombreOCodigo = materiaRepository.findByNombreIgnoringCaseOrCodigoIgnoringCase(nombre, codigo)
        if (existeConNombreOCodigo.isPresent) {
            throw ExcepcionUNQUE("La materia que desea crear con nombre $nombre " +
                    "y codigo $codigo, genera conflicto con la materia: ${existeConNombreOCodigo.get().nombre}, codigo: ${existeConNombreOCodigo.get().codigo}")
        } else {
            val materiasCorrelativas = materiaRepository.findAllByCodigoIn(correlativas)
            val materiaInexistente = correlativas.find { !materiasCorrelativas.map { c -> c.codigo }.contains(it) }
            if (materiaInexistente != null) throw ExcepcionUNQUE("No existe la materia con codigo: $materiaInexistente")
            val materia = materiaRepository.save(Materia(codigo, nombre, materiasCorrelativas.toMutableList(), carrera))
            materia.correlativas.forEach { it.correlativas.size }
            return materia
        }
    }

    @Transactional
    fun todas(): List<Materia> {
        val materias = materiaRepository.findAll().toList()
        materias.forEach { it.correlativas.size }
        return materias
    }

    @Transactional
    fun obtener(codigo: String): Materia {
        val materia = materiaRepository.findMateriaByCodigo(codigo).orElseThrow{ MateriaNoEncontradaExcepcion() }
        materia.correlativas.size
        return materia
    }

}
