package ar.edu.unq.postinscripciones.webservice.controller

import io.swagger.annotations.ApiOperation
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
@EnableAutoConfiguration
class BasicController {

    @ApiOperation(value = "Endpoint used to show Re-Senia API docs.", hidden = true)
    @RequestMapping(value = ["/",""], method = [RequestMethod.GET])
    fun postinscripcionesAPIDocs(): RedirectView {
        return RedirectView("/swagger-ui.html")
    }
}