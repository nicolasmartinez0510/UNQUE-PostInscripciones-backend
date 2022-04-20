package ar.edu.unq.postinscripciones.webservice.config

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.Oferta
import ar.edu.unq.postinscripciones.persistence.MateriaRepository
import ar.edu.unq.postinscripciones.persistence.OfertaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class DataSeed(@Autowired private val materiaRepository: MateriaRepository,
			   @Autowired private val ofertaRepository: OfertaRepository) : CommandLineRunner {

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
			val oferta1 = Oferta(materia1, 35)
			val oferta2 = Oferta(materia2, 60)
			val oferta3 = Oferta(materia3, 30)
			val oferta4 = Oferta(materia4, 40)
			val oferta5 = Oferta(materia5, 30)

			materiaRepository.saveAll(listOf(materia1, materia2, materia3, materia4, materia5))
			ofertaRepository.saveAll(listOf(oferta1, oferta2, oferta3, oferta4, oferta5))
			println()
			println("##########################")
			println("Datos creados exitosamente")
			println("##########################")
			println()
		}
		println("Total de materias: ${materiaRepository.count()}")
		println("Total de ofertas: ${ofertaRepository.count()}")
	}

	private fun emptyData() : Boolean{
		return materiaRepository.count().toInt() == 0 && ofertaRepository.count().toInt() == 0
	}
}