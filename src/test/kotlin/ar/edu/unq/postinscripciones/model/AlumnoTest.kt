package ar.edu.unq.postinscripciones.model

import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AlumnoTest {
    lateinit var alumno: Alumno
    lateinit var comisionBdd: Comision
    lateinit var otraComision: Comision

    @BeforeEach
    fun `set up`() {
        comisionBdd = Comision()
        otraComision = Comision(numero = 5)
        alumno = Alumno()
    }

    @Test
    fun `un alumno puede completar un formulario de solicitud de cupo por cuatrimestre`() {
        val otraComision = Comision()
        val formulario = Formulario(comisionBdd.cuatrimestre, listOf())

        alumno.guardarFormulario(formulario)

        assertThat(alumno.llenoElFormularioDelCuatrimestre(comisionBdd.cuatrimestre)).isTrue
        assertThat(alumno.haSolicitado(otraComision)).isFalse
    }

    @Test
    fun `un alumno no puede tener dos formularios de sobrecupos del mismo cuatrimestre`() {
        val formulario = Formulario(comisionBdd.cuatrimestre)
        alumno.guardarFormulario(formulario)

        val excepcion = assertThrows<ExcepcionUNQUE> { alumno.guardarFormulario(formulario) }

        assertThat(excepcion.message).isEqualTo("Ya has solicitado materias para este cuatrimestre")
    }

    @Test
    fun `un alumno puede solicitar cupo para mas de una comision`() {
        val formulario = Formulario(
            comisionBdd.cuatrimestre,
            listOf(
                SolicitudSobrecupo(comisionBdd),
                SolicitudSobrecupo(otraComision)
            )
        )

        alumno.guardarFormulario(formulario)

        assertThat(alumno.haSolicitado(comisionBdd)).isTrue
        assertThat(alumno.haSolicitado(otraComision)).isTrue
    }

    @Test
    fun `un alumno conoce su legajo`() {
        alumno = Alumno(legajo = 90)

        assertThat(alumno.legajo).isEqualTo(90)
    }

    @Test
    fun `un alumno conoce su informacion`() {
        alumno = Alumno(nombre = "fede")

        assertThat(alumno.nombre).isEqualTo("fede")
    }

    @Test
    fun `un alumno conoce su apellido`() {
        alumno = Alumno(apellido = "generico")

        assertThat(alumno.apellido).isEqualTo("generico")
    }

    @Test
    fun `un alumno conoce su correo`() {
        alumno = Alumno(correo = "correo@correo.com")

        assertThat(alumno.correo).isEqualTo("correo@correo.com")
    }

    @Test
    fun `un alumno conoce su contrasenia`() {
        alumno = Alumno(contrasenia = "123")

        assertThat(alumno.contrasenia).isEqualTo("123")
    }

    @Test
    fun `un alumno conoce su dni`() {
        alumno = Alumno(dni = 123)

        assertThat(alumno.dni).isEqualTo(123)
    }
}