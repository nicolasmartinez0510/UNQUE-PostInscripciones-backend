package ar.edu.unq.postinscripciones.webservice.controller

import ar.edu.unq.postinscripciones.service.OfertaService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@ServiceREST
@RequestMapping("/api/oferta")
class OfertaController {
    @Autowired
    private lateinit var ofertaService: OfertaService

    @ApiOperation("Endpoint utilizado para poder registrar una nueva oferta academica en el sistema")
    @RequestMapping(value = ["/crear"], method = [RequestMethod.POST])
    fun registrarOferta(@RequestBody formularioOferta: FormularioOferta): ResponseEntity<*> {
        ofertaService.crearOfertaPara(formularioOferta.idMateria, formularioOferta.cuposTotales)

        return ResponseEntity(null, HttpStatus.CREATED)
    }

    @ApiOperation("Endpoint utilizado para asignar cupo a una oferta existente")
    @RequestMapping(value = ["/asignar"], method = [RequestMethod.POST])
    fun asignarCupo(@ApiParam(value = "Id de la oferta", example = "1", required = true)
                    @RequestParam idOferta: Long): ResponseEntity<*> {
        return ResponseEntity(ofertaService.asignarCupo(idOferta), HttpStatus.OK)
    }
}

data class FormularioOferta(val idMateria: Long, val cuposTotales: Int)
