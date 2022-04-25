package ar.edu.unq.postinscripciones.webservice.controller

import ar.edu.unq.postinscripciones.service.FormularioService
import ar.edu.unq.postinscripciones.service.Solicitud
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@ServiceREST
@RequestMapping("/api/solicitud")
class FormularioController {
    @Autowired
    private lateinit var formularioService: FormularioService

    @ApiOperation("Endpoint que se usa para generar un formulario con solicitudes de materias")
    @RequestMapping(value = ["/crear"], method = [RequestMethod.POST])
    fun solicitarCupo(@RequestBody solicitud: FormularioSolicitud): ResponseEntity<*> {
        return ResponseEntity(
            formularioService.crear(solicitud.idCuatrimestre, solicitud.solicitudes),
            HttpStatus.CREATED
        )
    }
}

data class FormularioSolicitud(val idCuatrimestre: Long, val solicitudes: List<Solicitud>) {

}