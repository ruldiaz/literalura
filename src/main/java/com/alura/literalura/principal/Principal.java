package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();


    private List<DatosLibro> datosLibros = new ArrayList<>();

    private LibroRepository repositorio;
    private AutorRepository repositorioAutor;

    private List<Libro> libros;
    private Optional<Libro> libroBuscado;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repositorio = repository;
        this.repositorioAutor = autorRepository;
    }

    public void muestraElMenu() {

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    Elija la opción a través de su número:
                    
                    1 - Buscar libro por titulo
                    2 - listar libros registrados
                    3 - listar autores registrados
                    4 - listar autores vivos en un determinado año
                    5 - listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
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
                    listarAutoresVivosEnUnAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);

        if (datos.resultados().isEmpty()) {
            System.out.println("Libro no encontrado");
        } else {
            // Si hay resultados, procesamos el primer libro
            System.out.println(datos);

            // Crear un objeto Libro a partir de los datos
            DatosLibro datosLibro = datos.resultados().get(0);
            Libro libro = new Libro(datosLibro);

            // Verificar si el libro ya existe en la base de datos (por ejemplo, usando el título)
            Optional<Libro> libroExistente = repositorio.findByTitulo(libro.getTitulo());
            if (libroExistente.isPresent()) {
                // Si el libro ya existe, mostrar un mensaje y no guardarlo
                System.out.println("El libro '" + libro.getTitulo() + "' ya está registrado.");
            } else {
                // Aseguramos que los autores están correctamente asignados al libro
                for (Autor autor : libro.getAutores()) {
                    autor.setLibro(libro);  // Asignamos el libro a cada autor
                }

                // Guardar el libro en la base de datos
                repositorio.save(libro);

                // Mostrar los datos del libro guardado
                System.out.println("----- LIBRO -----");
                System.out.println("Titulo: " + libro.getTitulo());

                // Mostrar los autores asociados al libro
                System.out.print("Autor: ");
                if (libro.getAutores() != null && !libro.getAutores().isEmpty()) {
                    for (Autor autor : libro.getAutores()) {
                        System.out.println(autor.getNombre());
                    }
                } else {
                    System.out.println("No hay autores registrados.");
                }

                // Mostrar los idiomas
                System.out.print("Idioma: ");
                if (libro.getIdiomas() != null && !libro.getIdiomas().isEmpty()) {
                    System.out.println(String.join(", ", libro.getIdiomas()));
                } else {
                    System.out.println("No hay idiomas registrados.");
                }

                // Mostrar el número de descargas
                System.out.println("Numero de descargas: " + (libro.getNumeroDeDescargas() != null ? libro.getNumeroDeDescargas() : "N/A"));
                System.out.println("-----------------");

                System.out.println("Libro guardado con éxito");
            }
        }
    }




    private void listarLibrosRegistrados() {
        System.out.println("Listado de libros registrados:");

        // Obtener todos los libros desde el repositorio
        libros = repositorio.findAll();  // Esto obtiene todos los libros de la base de datos

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            // Recorrer los libros y mostrar la información de cada uno
            for (Libro libro : libros) {
                System.out.println("----- LIBRO -----");
                System.out.println("Titulo: " + libro.getTitulo());

                // Mostrar los autores asociados al libro
                System.out.print("Autor: ");
                if (libro.getAutores() != null && !libro.getAutores().isEmpty()) {
                    for (Autor autor : libro.getAutores()) {
                        System.out.println(autor.getNombre());
                    }
                } else {
                    System.out.println("No hay autores registrados.");
                }

                // Mostrar los idiomas
                System.out.print("Idioma: ");
                if (libro.getIdiomas() != null && !libro.getIdiomas().isEmpty()) {
                    System.out.println(String.join(", ", libro.getIdiomas()));
                } else {
                    System.out.println("No hay idiomas registrados.");
                }

                // Mostrar el número de descargas
                System.out.println("Numero de descargas: " + (libro.getNumeroDeDescargas() != null ? libro.getNumeroDeDescargas() : "N/A"));
                System.out.println("-----------------");
            }
        }
    }

    private void listarAutoresRegistrados() {
        System.out.println("Listado de autores registrados:");
        System.out.println("");
        // Obtener todos los autores desde el repositorio
        List<Autor> autores = repositorioAutor.findAll();  // Aquí debería ser autorRepository.findAll() si tienes el repositorio de autores.

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            // Recorrer los autores y mostrar la información de cada uno
            for (Autor autor : autores) {

                System.out.println("Autor: " + autor.getNombre());

                // Mostrar la fecha de nacimiento
                System.out.println("Fecha de nacimiento: " + (autor.getAnioDeNacimiento() != null ? autor.getAnioDeFallecimiento() : "Desconocida"));

                // Mostrar la fecha de fallecimiento
                System.out.println("Fecha de fallecimiento: " + (autor.getAnioDeFallecimiento() != null ? autor.getAnioDeFallecimiento() : "Desconocida"));

                // Mostrar los libros asociados al autor
                System.out.print("Libros: ");
                if (autor.getLibro() != null) {
                    System.out.println("[" + autor.getLibro().getTitulo() + "]");
                } else {
                    System.out.println("No hay libros registrados.");
                }

                System.out.println("");
            }
        }
    }

    public void listarAutoresVivosEnUnAnio() {
        // Preguntar el año
        int anioBusqueda = obtenerAño();

        System.out.println("Listado de autores vivos en el año " + anioBusqueda + ":");

        // Usar el método derivado del repositorio para obtener los autores
        List<Autor> autores = repositorioAutor.findByAnioDeFallecimientoGreaterThanEqualOrAnioDeFallecimientoIsNull(anioBusqueda);

        // Filtrar los autores cuya fecha de nacimiento sea inferior al año de búsqueda
        autores.removeIf(autor -> autor.getAnioDeNacimiento() != null && autor.getAnioDeNacimiento() >= anioBusqueda);

        // Mostrar los resultados
        autores.forEach(autor -> {
            System.out.println("----- AUTOR -----");
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Año de nacimiento: " + autor.getAnioDeNacimiento());
            System.out.println("Año de fallecimiento: " + (autor.getAnioDeFallecimiento() != null ? autor.getAnioDeFallecimiento() : "Desconocido"));

            // Mostrar los libros (Si existe una relación entre Autor y Libro)
            if (autor.getLibro() != null) {
                System.out.println("Libro: " + autor.getLibro().getTitulo());
            } else {
                System.out.println("No hay libros registrados.");
            }

            System.out.println("-----------------");
        });
    }


    private int obtenerAño() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el año de búsqueda: ");
        return scanner.nextInt();  // Tomar el año de búsqueda del usuario
    }

    public void listarLibrosPorIdioma() {
        // Pedimos al usuario que ingrese el idioma
        System.out.println("Ingrese el idioma para buscar los libros:");
        System.out.println("es - español");
        System.out.println("en - inglés");
        System.out.println("fr - francés");
        System.out.println("pt - portugués");

        String idiomaBuscado = teclado.nextLine().trim();  // Leemos el idioma ingresado por el usuario

        // Validamos si el idioma ingresado es uno de los permitidos
        List<String> idiomasPermitidos = List.of("es", "en", "fr", "pt");
        if (!idiomasPermitidos.contains(idiomaBuscado)) {
            System.out.println("Idioma no válido. Intenta nuevamente.");
            return;
        }

        // Obtenemos los libros por idioma usando el método derivado del repositorio
        List<Libro> librosPorIdioma = repositorio.findByIdioma(idiomaBuscado);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros para el idioma " + idiomaBuscado);
        } else {
            // Si encontramos libros, mostramos los resultados
            for (Libro libro : librosPorIdioma) {
                System.out.println("----- LIBRO -----");
                System.out.println("Titulo: " + libro.getTitulo());

                // Mostrar los autores asociados al libro
                System.out.print("Autor: ");
                if (libro.getAutores() != null && !libro.getAutores().isEmpty()) {
                    for (Autor autor : libro.getAutores()) {
                        System.out.println(autor.getNombre());
                    }
                } else {
                    System.out.println("No hay autores registrados.");
                }

                // Mostrar los idiomas
                System.out.print("Idioma: ");
                if (libro.getIdiomas() != null && !libro.getIdiomas().isEmpty()) {
                    System.out.println(String.join(", ", libro.getIdiomas()));
                } else {
                    System.out.println("No hay idiomas registrados.");
                }

                // Mostrar el número de descargas
                System.out.println("Numero de descargas: " + (libro.getNumeroDeDescargas() != null ? libro.getNumeroDeDescargas() : "N/A"));
                System.out.println("-----------------");
            }
        }
    }



}
