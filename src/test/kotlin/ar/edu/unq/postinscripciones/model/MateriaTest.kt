package ar.edu.unq.postinscripciones.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MateriaTest {
    lateinit var bdd: Materia

    @BeforeEach
    fun `set up`() {
        bdd = Materia("PW-1050", "Bases de Datos")
    }

    @Test
    fun `una materia conoce su codigo codigo`() {
        assertThat(bdd.codigo).isEqualTo("PW-1050")
    }

    @Test
    fun `una materia conoce su codigo nombre`() {
        assertThat(bdd.nombre).isEqualTo("Bases de Datos")
    }
}
