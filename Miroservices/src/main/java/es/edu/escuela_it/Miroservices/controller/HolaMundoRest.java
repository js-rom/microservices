package es.edu.escuela_it.Miroservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import es.edu.escuela_it.Miroservices.configuration.ApplicationConfig;

@RestController
public class HolaMundoRest {

	@Autowired
	private ApplicationConfig appConfig;

	@GetMapping("/holaMundo")
	public String saludo() {
		System.out.println(this.appConfig.toString());
		return "Hola Mundo servicio Rest Java";
		
	}
}
