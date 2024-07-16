package com.aluracursos.gutendex_menu;

import com.aluracursos.gutendex_menu.principal.Principal;
import com.aluracursos.gutendex_menu.repository.AutorRepository;
import com.aluracursos.gutendex_menu.repository.LibroRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GutendexMenuApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;
	public static void main(String[] args) {
		SpringApplication.run(GutendexMenuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepository, autorRepository);
		principal.ejecutarPrograma();
	}
}
