package com.aluracursos.gutendex_menu.repository;

import com.aluracursos.gutendex_menu.model.Autor;
import com.aluracursos.gutendex_menu.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);
}
