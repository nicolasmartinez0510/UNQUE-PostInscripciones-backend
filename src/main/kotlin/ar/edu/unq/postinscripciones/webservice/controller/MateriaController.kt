package ar.edu.unq.postinscripciones.webservice.controller

import ar.edu.unq.postinscripciones.service.MateriaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/materia")
class MateriaController {
    @Autowired
    private lateinit var materiaService: MateriaService


    @RequestMapping(value = ["/crear"], method = [RequestMethod.POST])
    fun registrarMateria(@RequestBody formularioMateria: FormularioMateria): ResponseEntity<*> {
        return ResponseEntity(
            materiaService.crear(formularioMateria.nombre, formularioMateria.codigo),
            HttpStatus.CREATED
        )
    }

    @RequestMapping(value = ["/todas"], method = [RequestMethod.GET])
    fun asdasd(): ResponseEntity<*> {
        return ResponseEntity.ok(1)
    }
}

data class FormularioMateria(val nombre: String, val codigo: String)
