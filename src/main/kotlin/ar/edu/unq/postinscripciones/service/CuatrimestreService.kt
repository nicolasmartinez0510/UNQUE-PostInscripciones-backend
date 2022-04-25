package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.persistence.CuatrimestreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CuatrimestreService {

    @Autowired
    private lateinit var cuatrimestreRepository: CuatrimestreRepository

    @Transactional
    fun crear(anio: Int, semestre: Semestre): Cuatrimestre {
        try {
            return cuatrimestreRepository.save(Cuatrimestre(anio, semestre))
        } catch (excepcion: DataIntegrityViolationException) {
            throw ExcepcionUNQUE("Ya existe el cuatrimestre que desea crear.")
        }
    }
}