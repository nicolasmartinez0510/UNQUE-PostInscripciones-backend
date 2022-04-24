package ar.edu.unq.postinscripciones.resources

import ar.edu.unq.postinscripciones.persistence.*
import ar.edu.unq.postinscripciones.service.AlumnoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class DataService {

    @Autowired
    private lateinit var comisionRespository: ComisionRespository

    @Autowired
    private lateinit var materiaRepository: MateriaRepository

    @Autowired
    private lateinit var cuatrimestreRepository: CuatrimestreRepository

    @Autowired
    private lateinit var formularioRepository: FormularioRepository

    @Autowired
    private lateinit var alumnoRepository: AlumnoRepository

    @Transactional
    fun clearDataSet() {
        formularioRepository.deleteAll()
        comisionRespository.deleteAll()
        materiaRepository.deleteAll()
        cuatrimestreRepository.deleteAll()
        alumnoRepository.deleteAll()

    }
}