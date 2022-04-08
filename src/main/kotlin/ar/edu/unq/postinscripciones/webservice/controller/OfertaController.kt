package ar.edu.unq.postinscripciones.webservice.controller

import ar.edu.unq.postinscripciones.service.OfertaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/oferta")
class OfertaController {
    @Autowired
    private lateinit var ofertaService: OfertaService

    @RequestMapping(value = ["/crear"], method = [RequestMethod.POST])
    fun registrarOferta(@RequestBody formularioOferta: FormularioOferta): ResponseEntity<*> {
        ofertaService.crearOfertaPara(formularioOferta.idMateria, formularioOferta.cuposTotales)

        return ResponseEntity(null, HttpStatus.CREATED)
    }

    @RequestMapping(value = ["/asignar"], method = [RequestMethod.POST])
    fun asignarCupo(@RequestParam idOferta: Long): ResponseEntity<*> {
        return ResponseEntity(ofertaService.asignarCupo(idOferta), HttpStatus.OK)
    }
}

data class FormularioOferta(val idMateria: Long, val cuposTotales: Int)
