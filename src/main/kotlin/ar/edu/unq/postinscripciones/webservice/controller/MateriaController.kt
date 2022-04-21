package ar.edu.unq.postinscripciones.webservice.controller

import ar.edu.unq.postinscripciones.service.MateriaService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@ServiceREST
@RequestMapping("/api/materia")
class MateriaController {
    @Autowired
    private lateinit var materiaService: MateriaService

    @ApiOperation("Endpoint que se usa para registra una nueva materia en el sistema")
    @RequestMapping(value = ["/crear"], method = [RequestMethod.POST])
    fun registrarMateria(@RequestBody formularioMateria: FormularioMateria): ResponseEntity<*> {
        return ResponseEntity(
            materiaService.crear(formularioMateria.nombre, formularioMateria.codigo),
            HttpStatus.CREATED
        )
    }

    @ApiOperation(value = "Endpoint usado para listar todas las materias disponibles")
    @RequestMapping(value = ["/todas"], method = [RequestMethod.GET])
    fun asdasd(): ResponseEntity<*> {
        return ResponseEntity(
                materiaService.todas(),
                HttpStatus.OK
        )
    }
}

data class FormularioMateria(val nombre: String, val codigo: String)
