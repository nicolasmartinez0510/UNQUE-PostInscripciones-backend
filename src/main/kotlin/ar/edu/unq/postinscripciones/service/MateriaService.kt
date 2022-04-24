package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.persistence.MateriaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class MateriaService {
    @Autowired
    private lateinit var materiaRepository: MateriaRepository


    @Transactional
    fun crear(nombre: String, codigo: String): Materia {
        return materiaRepository.save(Materia(nombre, codigo))
    }

    @Transactional
    fun todas(): List<Materia> {
        return materiaRepository.findAll().toList()
    }

    @Transactional
    fun obtener(codigo: String): Materia {
        return materiaRepository.findMateriaByCodigo(codigo).orElseThrow{ MateriaInexistenteException() }
    }

}

class MateriaInexistenteException: RuntimeException("La materia no existe")
