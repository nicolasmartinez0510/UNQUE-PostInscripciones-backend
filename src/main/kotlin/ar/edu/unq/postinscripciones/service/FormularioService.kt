package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Formulario
import ar.edu.unq.postinscripciones.model.SolicitudSobrecupo
import ar.edu.unq.postinscripciones.model.cuatrimestre.CuatrimestreId
import ar.edu.unq.postinscripciones.persistence.ComisionRespository
import ar.edu.unq.postinscripciones.persistence.CuatrimestreRepository
import ar.edu.unq.postinscripciones.persistence.FormularioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class FormularioService {

    @Autowired
    private lateinit var formularioRepository: FormularioRepository

    @Autowired
    private lateinit var cuatrimestreRepository: CuatrimestreRepository

    @Autowired
    private lateinit var comisionRepository: ComisionRespository

    @Transactional
    fun crear(cuatrimestreId: CuatrimestreId, solicitudesSobrecupo: List<Solicitud>): Formulario {
        val cuatrimestre = cuatrimestreRepository.findById(cuatrimestreId).get()
        val comisiones = comisionRepository.findAllById(solicitudesSobrecupo.map { it.comisionId })
        return formularioRepository.save(Formulario(cuatrimestre, comisiones.map { SolicitudSobrecupo(it) }))
    }
}

data class Solicitud(
    val comisionId: Long,
)