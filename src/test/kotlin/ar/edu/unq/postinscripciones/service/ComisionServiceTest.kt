package ar.edu.unq.postinscripciones.service

import ar.edu.unq.postinscripciones.model.Materia
import ar.edu.unq.postinscripciones.model.comision.Comision
import ar.edu.unq.postinscripciones.model.comision.Dia
import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.model.exception.ExcepcionUNQUE
import ar.edu.unq.postinscripciones.resources.DataService
import ar.edu.unq.postinscripciones.service.dto.ComisionDTO
import ar.edu.unq.postinscripciones.service.dto.FormularioComision
import ar.edu.unq.postinscripciones.service.dto.FormularioCuatrimestre
import ar.edu.unq.postinscripciones.service.dto.HorarioDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
internal class ComisionServiceTest {

    @Autowired
    private lateinit var comisionService: ComisionService

    @Autowired
    private lateinit var cuatrimestreService: CuatrimestreService

    @Autowired
    private lateinit var materiaService: MateriaService

    @Autowired
    private lateinit var dataService: DataService

    private lateinit var bdd: Materia
    private lateinit var c1: Cuatrimestre
    private lateinit var horarios: List<HorarioDTO>
    private lateinit var comision: Comision

    @BeforeEach
    fun setUp() {
        bdd = materiaService.crear("Base de datos", "BBD-208")
        val formularioCuatrimestre = FormularioCuatrimestre(2022, Semestre.S1)
        c1 = cuatrimestreService.crear(formularioCuatrimestre)

        horarios = listOf(
            HorarioDTO(Dia.LUNES, "18:30", "21:30"),
            HorarioDTO(Dia.JUEVES, "18:30", "21:30")
        )

        val formulario = FormularioComision(
            1,
            bdd.codigo,
            2022,
            Semestre.S1,
            35,
            5,
            horarios
        )
        comision = comisionService.crear(formulario)
    }

    @Test
    fun `Se puede crear una comision`() {
        assertThat(comision).isNotNull
    }

    @Test
    fun `Se puede pedir todas las comisiones de una materia`() {
        assertThat(comisionService.obtenerComisionesMateria(bdd.codigo).first()).usingRecursiveComparison()
            .isEqualTo(ComisionDTO.desdeModelo(comision))
    }

    @Test
    fun `Se puede obtener una comision especifica`() {
        val comisionObtenida = comisionService.obtener(comision.id!!)

        assertThat(comisionObtenida).usingRecursiveComparison().isEqualTo(ComisionDTO.desdeModelo(comision))
    }

    @Test
    fun `No se puede obtener una comision que no existe`() {
        val exception = assertThrows<ExcepcionUNQUE> { comisionService.obtener(99999) }

        assertThat(exception.message).isEqualTo("No se encuentra la comision")
    }

    @Test
    fun `No se pueden obtener las comisiones de una materia que no existe`() {
        val exception = assertThrows<ExcepcionUNQUE> { comisionService.obtenerComisionesMateria("AA-209") }

        assertThat(exception.message).isEqualTo("No se encuentra la materia")
    }

    @AfterEach
    fun tearDown() {
        dataService.clearDataSet()
    }

}