package com.alura.literalura.service;

import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    public Libro obtenerLibroPorId(Long id) {
        return libroRepository.findById(id).orElse(null);
    }

    public Libro guardarLibro(Libro libro) {
        try {
            return libroRepository.save(libro);
        } catch (DataIntegrityViolationException e) {
            // Manejo de la excepción, puedes registrar el error o lanzar una excepción personalizada
            System.out.println("Error de integridad de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar el libro debido a un error de integridad de datos");
        }
    }

    public void eliminarLibro(Long id) {
        try {
            libroRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // Manejo de la excepción en caso de error al intentar eliminar
            System.out.println("Error de integridad de datos al eliminar: " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar el libro debido a un error de integridad de datos");
        }
    }
}
