package com.aluracursos.gutendex_menu.principal;
import com.aluracursos.gutendex_menu.model.*;
import com.aluracursos.gutendex_menu.repository.AutorRepository;
import com.aluracursos.gutendex_menu.repository.LibroRepository;
import com.aluracursos.gutendex_menu.service.ConsumoAPI;
import com.aluracursos.gutendex_menu.service.ConvierteDatos;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private String URL_BASE = "https://gutendex.com/books/";
    private String SEARCH = "?search=";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void Menu() {
        var seleccion = -1;
        while (seleccion != 0) {
            var menu = """
            *********************************************
            1 - Buscar un libro por título
            2 - Lista de libros registrados
            3 - Lista de autores registrados
            4 - Lista de autores registrados por año
            5 - Lista de libros registrados por idioma
            6 - Salir
            *********************************************
            """;

            System.out.println(menu);
            try {
                seleccion = Integer.valueOf(teclado.nextLine());
                switch (seleccion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresRegistradosPorAno();
                        break;
                    case 5:
                        listarLibrosRegistradosPorIdioma();
                        break;
                    case 6:
                        System.out.println("Saliendo del programa");
                        break;
                    default:
                        System.out.println("Seleccione una opción disponible");
                        continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Seleccione una opción disponible");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro:");
        var LibroTitulo = teclado.nextLine();
        var Consulta = consumoApi.obtenerDatos(URL_BASE + SEARCH + LibroTitulo.replace(" ", "%20").replace("'", "%27"));
        DatosGenerales consultaDatosGenerales = conversor.convertirDatos(Consulta, DatosGenerales.class);
        if (!consultaDatosGenerales.resultados().isEmpty()) {
            DatosLibro consultaDatosLibro = consultaDatosGenerales.resultados().get(0);
            Autor consultaAutor = new Autor(consultaDatosLibro);
            Libro consultaLibro = new Libro(consultaDatosLibro);
            consultaAutor.setLibros(consultaLibro);
            Optional<Libro> libroRegistrado = libroRepository.findByTituloContainsIgnoreCase(consultaLibro.getTitulo());
            if (libroRegistrado.isPresent()) {
                System.out.println("El libro ha sido registrado");
            } else {
                Optional<Autor> autorRegistrado = autorRepository.findByAutor(consultaAutor.getAutor());
                if (autorRegistrado.isPresent()) {
                    consultaLibro.setAuthor(autorRegistrado.get());
                    libroRepository.save(consultaLibro);
                } else {
                    autorRepository.save(consultaAutor);
                    libroRepository.save(consultaLibro);
                }
                System.out.println(consultaLibro);
            }
        } else {
            System.out.println("El libro no ha sido encontrado");
        }
    }

    private void listarLibrosRegistrados() {
        libroRepository.findAll().forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> Autores = autorRepository.findAll();
        Autores.forEach(System.out::println);
    }
    private void listarAutoresRegistradosPorAno() {
        System.out.println("Ingrese el año:");
        var ano = Integer.valueOf(teclado.nextLine());
        List<Autor> autores = autorRepository.listarAutoresRegistradosVivosPorAno(ano);
        if (autores.isEmpty()) {
            System.out.println("No existe un autor registrado");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarLibrosRegistradosPorIdioma() {
        List<String> Idioma = libroRepository.findAll().stream()
                .map(l -> l.getIdioma())
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Selecciona el idioma: ");
        Idioma.forEach(System.out::println);
        var idiomaSeleccionado = teclado.nextLine();
        if (Idioma.contains(idiomaSeleccionado)) {
            List<Libro> librosPorIdioma = libroRepository.findAll().stream()
                    .filter(l -> l.getIdioma().equals(idiomaSeleccionado))
                    .collect(Collectors.toList());
            librosPorIdioma.forEach(System.out::println);
        } else {
            System.out.println("Opción no válida");
        }
    }

}
