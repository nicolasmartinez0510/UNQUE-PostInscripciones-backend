package ar.edu.unq.postinscripciones.webservice.controller

import ar.edu.unq.postinscripciones.model.cuatrimestre.Cuatrimestre
import ar.edu.unq.postinscripciones.model.cuatrimestre.Semestre
import ar.edu.unq.postinscripciones.service.ComisionService
import ar.edu.unq.postinscripciones.service.dto.OfertaAcademicaDTO
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ServiceREST
@RequestMapping("/api/comision")
class ComisionController {
    @Autowired
    private lateinit var comisionService: ComisionService

    @ApiOperation("Endpoint que se usa para registrar nuevas comisiones en el sistema")
    @RequestMapping(value = [""], method = [RequestMethod.POST])
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Comision creada"),
            ApiResponse(code = 400, message = "Algo salio mal")
        ]
    )
    fun actualizarOfertaAcademica(
        @RequestBody oferta: OfertaAcademicaDTO,
    ): ResponseEntity<*> {
        return ResponseEntity(
            comisionService.actualizarOfertaAcademica(oferta.comisionesACargar, oferta.inicioInscripciones, oferta.finInscripciones),
            HttpStatus.OK
        )
    }

    @ApiOperation(value = "Endpoint usado para listar todas las comisiones de una materia especifica")
    @RequestMapping(value = ["/materia/{codigoMateria}"], method = [RequestMethod.GET])
    fun materiasComision(
        @PathVariable
        @ApiParam(value = "Codigo de la materia", example = "0", required = true)
        codigoMateria: String
    ): ResponseEntity<*> {
        return ResponseEntity(
            comisionService.obtenerComisionesMateria(codigoMateria),
            HttpStatus.OK
        )
    }

    @ApiOperation(value = "Endpoint usado para listar todas las comisiones de un cuatrimestre ordenadas por cantidad de solicitudes")
    @RequestMapping(value = [""], method = [RequestMethod.GET])
    fun comisionSolicitudes(
        @ApiParam(value = "Anio del cuatrimestre", example = "2022", required = true)
        @RequestParam
        anio: Int,
        @ApiParam(value = "Semestre del cuatrimestre", example = "S1", required = true)
        @RequestParam
        semestre: Semestre
    ): ResponseEntity<*> {
        return ResponseEntity(
            comisionService.comisionesPorSolicitudes(Cuatrimestre(anio, semestre)),
            HttpStatus.OK
        )
    }
}