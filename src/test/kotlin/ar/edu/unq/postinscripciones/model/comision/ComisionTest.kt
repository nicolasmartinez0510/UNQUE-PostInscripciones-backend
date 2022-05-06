package ar.edu.unq.postinscripciones.model.comision

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalTime

internal class ComisionTest {
    lateinit var bdd: Materia
    lateinit var comisionUnoBdd: Comision

    val cuposTotales = 25
    val cuposOcupados = 20
    val sobrecuposTotales = 15
    val cuatrimestre = Cuatrimestre(2022, Semestre.S2)

    @BeforeEach
    fun `set up`() {
        val horarios: List<Horario> = horariosBdd()
        bdd = Materia()
        comisionUnoBdd = Comision(bdd, 1, cuatrimestre, horarios, cuposTotales, cuposOcupados, sobrecuposTotales)
    }

    @Test
    fun `una comision pertenece a una materia`() {
        assertThat(comisionUnoBdd.materia).isEqualTo(bdd)
    }

    @Test
    fun `una comision posee un numero de comision`() {
        assertThat(comisionUnoBdd.numero).isEqualTo(1)
    }

    @Test
    fun `una comision conoce en que cuatrimestre se dicta`() {
        assertThat(comisionUnoBdd.cuatrimestre).isEqualTo(cuatrimestre)
    }

    @Test
    fun `una comision conoce sus cupos totales`() {
        assertThat(comisionUnoBdd.cuposTotales).isEqualTo(25)
    }

    @Test
    fun `una comision conoce sus cupos ocupados`() {
        assertThat(comisionUnoBdd.cuposOcupados).isEqualTo(20)
    }

    @Test
    fun `una comision conoce sus horarios`() {
        assertThat(comisionUnoBdd.horarios).usingRecursiveComparison().isEqualTo(horariosBdd())
    }

    @Test
    fun `una comision conoce sus sobrecupos totales`() {
        assertThat(comisionUnoBdd.sobrecuposTotales).isEqualTo(15)
    }

    @Test
    fun `una comision conoce sus cupos disponibles`() {
        val cuposDisponiblesDeseados = cuposTotales + sobrecuposTotales - cuposOcupados

        assertThat(comisionUnoBdd.cuposDisponibles()).isEqualTo(cuposDisponiblesDeseados)
    }

    fun horariosBdd(): List<Horario> {
        return listOf(
            Horario(Dia.LUNES, LocalTime.of(18, 0), LocalTime.of(21, 0)),
            Horario(Dia.MIERCOLES, LocalTime.of(12, 0), LocalTime.of(15, 0))
        )
    }
}