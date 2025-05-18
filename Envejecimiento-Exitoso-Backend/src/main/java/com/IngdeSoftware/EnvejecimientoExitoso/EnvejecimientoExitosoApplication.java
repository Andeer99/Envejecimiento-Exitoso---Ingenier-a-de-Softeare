package com.IngdeSoftware.EnvejecimientoExitoso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class EnvejecimientoExitosoApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(EnvejecimientoExitosoApplication.class);
		// Si Railway (u otro host) inyecta la var PORT, arrancamos en ese puerto:
		String port = System.getenv("PORT");
		if (port != null && !port.isBlank()) {
			app.setDefaultProperties(
					Collections.singletonMap("server.port", port)
			);
		}
		app.run(args);
	}

}
