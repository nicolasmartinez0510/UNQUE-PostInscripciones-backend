package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Oferta
import ar.edu.unq.postinscripciones.persistence.MateriaRepository
import ar.edu.unq.postinscripciones.persistence.OfertaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class OfertaService {

    @Autowired
    private lateinit var ofertaRepository: OfertaRepository

    @Autowired
    private lateinit var materiaRepository: MateriaRepository

    @Transactional
    fun crearOfertaPara(idMateria: String, cuposTotales: Int): Oferta {
        val materiaSolicitada = materiaRepository.findMateriaByCodigo(idMateria).orElseThrow{ MateriaInexistenteException() }

        return ofertaRepository.save(Oferta(materiaSolicitada, cuposTotales))
    }

    @Transactional
    fun asignarCupo(idOferta: Long) {
        ofertaRepository.findById(idOferta).get().tomarCupo()
    }

    @Transactional
    fun clearDataSet() {
        ofertaRepository.deleteAll()
    }


}
