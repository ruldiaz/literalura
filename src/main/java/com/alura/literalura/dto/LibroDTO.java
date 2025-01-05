package com.alura.literalura.dto;

import com.alura.literalura.model.Autor;

import java.util.List;

public record LibroDTO(
    Long id,
    String titulo,
    List<Autor> autores,
    List<String> idiomas,
    Integer numeroDeDescargas
) {
}
