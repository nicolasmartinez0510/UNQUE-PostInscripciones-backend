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
class AuthController {
	@RequestMapping(value = ["/hola"], method = [RequestMethod.GET])
	fun helloWorld(): ResponseEntity<*> {
		return ResponseEntity.ok("Hola!")
	}

}