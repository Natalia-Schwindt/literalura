package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Scanner;

public class Principal {
    private final Scanner lectura = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final LibroRepository repository;

    public Principal(LibroRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    """;
            System.out.println(menu);

            try {
                opcion = Integer.parseInt(lectura.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción no válida");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosPorFecha();
                case 5 -> listarLibrosPorIdioma();
                case 0 -> System.out.println("Cerrando la aplicación...");
                default -> System.out.println("Opción no válida");
            }
        }
    }

    private Datos getDatosLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var nombreLibro = lectura.nextLine();
        String URL_BASE = "https://gutendex.com/books/";
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        return conversor.obtenerDatos(json, Datos.class);
    }

    private void buscarLibroPorTitulo() {
        Datos datos = getDatosLibro();
        if (!datos.resultados().isEmpty()) {
            DatosLibro primerLibro = datos.resultados().get(0);
            Libro libro = new Libro(primerLibro);
            repository.save(libro);
            System.out.println(libro);
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = repository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {

    }

    private void listarAutoresVivosPorFecha() {

    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);
        var idioma = lectura.nextLine();
        List<Libro> librosPorIdioma = repository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma en la base de datos.");
        } else {
            librosPorIdioma.forEach(System.out::println);
        }
    }
}