package ar.edu.unq.postinscripciones.webservice.config

import ar.edu.unq.postinscripciones.model.Alumno
import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Dia
import ar.edu.unq.postinscripciones.model.comision.Horario
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.persistence.AlumnoRepository
import ar.edu.unq.postinscripciones.persistence.ComisionRespository
import ar.edu.unq.postinscripciones.persistence.CuatrimestreRepository
import ar.edu.unq.postinscripciones.persistence.MateriaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.LocalTime

@Profile("!test")
@Component
class DataSeed(
    @Autowired private val materiaRepository: MateriaRepository,
    @Autowired private val cuatrimestreRepository: CuatrimestreRepository,
    @Autowired private val comisionRespository: ComisionRespository,
    @Autowired private val alumnoRepository: AlumnoRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        loadData()
    }

    private fun loadData() {

        if (emptyData()) {
            val materia1 = Materia("0", "Base de Datos")
            val materia2 = Materia("1", "Introduccion a la Programación")
            val materia3 = Materia("2", "Organización de las Computadoras")
            val materia4 = Materia("3", "Matemática 1")
            val materia5 = Materia("4", "Estructura de Datos")
            val bddhorariosc1 = listOf(
                Horario(Dia.MARTES, LocalTime.of(10, 0, 0), LocalTime.of(12, 0, 0)),
                Horario(Dia.JUEVES, LocalTime.of(10, 0, 0), LocalTime.of(12, 0, 0))
            )

            val bddhorariosc2 = listOf(
                Horario(Dia.LUNES, LocalTime.of(10, 0, 0), LocalTime.of(12, 0, 0)),
                Horario(Dia.MIERCOLES, LocalTime.of(10, 0, 0), LocalTime.of(12, 0, 0))
            )

            val matehorarios = listOf(
                Horario(Dia.LUNES, LocalTime.of(10, 30, 0), LocalTime.of(12, 30, 0)),
                Horario(Dia.JUEVES, LocalTime.of(10, 30, 0), LocalTime.of(12, 30, 0))
            )

            val estrhorarios = listOf(
                Horario(Dia.LUNES, LocalTime.of(9, 0, 0), LocalTime.of(12, 0, 0)),
                Horario(Dia.MIERCOLES, LocalTime.of(9, 0, 0), LocalTime.of(12, 0, 0)),
                Horario(Dia.VIERNES, LocalTime.of(9, 0, 0), LocalTime.of(12, 0, 0))
            )


            val cuatrimestre = Cuatrimestre(2022, Semestre.S1)

            val bddc1 = Comision(materia1, 1, cuatrimestre, bddhorariosc1)
            val bddc2 = Comision(materia1, 2, cuatrimestre, bddhorariosc2)
            val matec1 = Comision(materia4, 1, cuatrimestre, matehorarios)
            val estrc1 = Comision(materia5, 1, cuatrimestre, estrhorarios)

            val jorge = Alumno(
                12345,
                "Jorge",
                "Arenales",
                "jorge.arenales20@alu.edu.ar",
                12345678,
                "contrasenia"
            )

            cuatrimestreRepository.save(cuatrimestre)
            materiaRepository.saveAll(listOf(materia1, materia2, materia3, materia4, materia5))
            comisionRespository.saveAll(listOf(bddc1, bddc2, matec1, estrc1))
            alumnoRepository.save(jorge)

            val cantMaterias = materiaRepository.count()
            val cantComisiones = comisionRespository.count()
            val cantAlumnos = alumnoRepository.count()

            println()
            println("##########################")
            println("Datos creados exitosamente")
            println("##########################")
            println()
            println("Total de materias: $cantMaterias")
            println("Total de comisiones: $cantComisiones")
            println("Total de alumnos: $cantAlumnos")
        }
    }

    private fun emptyData(): Boolean {
        return materiaRepository.count().toInt() == 0
    }


}