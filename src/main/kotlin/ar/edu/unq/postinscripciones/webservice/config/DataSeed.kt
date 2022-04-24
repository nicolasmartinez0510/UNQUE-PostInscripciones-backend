package ar.edu.unq.postinscripciones.webservice.config

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.persistence.CuatrimestreRepository
import ar.edu.unq.postinscripciones.persistence.MateriaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("!test")
@Component
class DataSeed(
    @Autowired private val materiaRepository: MateriaRepository,
    @Autowired private val cuatrimestreRepository: CuatrimestreRepository
) : CommandLineRunner {

    @Throws(Exception::class)
    override fun run(vararg args: String?) {
        loadData()
    }

    private fun loadData() {

        if (emptyData()) {
            val materia1 = Materia("0", "Base de Datos")
            val materia2 = Materia("1", "Introduccion a la Programación")
            val materia3 = Materia("2", "Organización de las Computadoras")
            val materia4 = Materia("3", "Matemática 1")
            val materia5 = Materia("4", "Estructura de Datos")

            val cuatrimestre = Cuatrimestre(2022, Semestre.S1)

            cuatrimestreRepository.save(cuatrimestre)
            materiaRepository.saveAll(listOf(materia1, materia2, materia3, materia4, materia5))

            println()
            println("##########################")
            println("Datos creados exitosamente")
            println("##########################")
            println()
        }
        println("Total de materias: ${materiaRepository.count()}")
    }

    private fun emptyData(): Boolean {
        return materiaRepository.count().toInt() == 0
    }
}