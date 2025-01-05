package com.alura.literalura.model;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", unique = true, nullable = false, length = 255)
    private String titulo;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;

    @Column(name = "idioma", nullable = true)
    private String idioma;
    private Integer numeroDeDescargas;

    public Libro() {}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        //this.autores = datosLibro.autores();
        //this.idiomas = datosLibro.idiomas();

        if (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) {
            this.idioma = datosLibro.idiomas().get(0);  // Solo el primer idioma
        } else {
            this.idioma = null;  // Si no hay idiomas, dejamos el campo en null
        }

        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
        // Solo tomamos el primer autor si existe
        if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
            this.autores = List.of(datosLibro.autores().get(0));
        } else {
            this.autores = null;
        }
    }

    @Override
    public String toString() {
        return "Libro{" +
                ", titulo='" + titulo + '\'' +
                ", autores=" + autores +
                ", idiomas=" + idioma +
                ", numeroDeDescargas=" + numeroDeDescargas +
                '}';
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public String getIdiomas() {
        return idioma;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idioma = idioma;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
}
