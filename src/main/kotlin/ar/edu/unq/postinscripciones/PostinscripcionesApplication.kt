package ar.edu.unq.postinscripciones

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class PostinscripcionesApplication

fun main(args: Array<String>) {
	runApplication<PostinscripcionesApplication>(*args)
}

@RestController
@RequestMapping("/api")
class HolaMundoController {
	@RequestMapping(value = ["/hola"], method = [RequestMethod.GET])
	fun helloWorld(): ResponseEntity<*> {
		return ResponseEntity.ok("Hola mundo!")
	}

}


@RestController
@RequestMapping("/opa")
class QueOndaController {
	@RequestMapping(value = ["/holis"], method = [RequestMethod.GET])
	fun todoBien(): ResponseEntity<*> {
		return ResponseEntity.ok("Que onda?")
	}

}