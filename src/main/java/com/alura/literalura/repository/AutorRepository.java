package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Buscar autores vivos en un año dado
    public List<Autor> findByAnioDeFallecimientoGreaterThanEqualOrAnioDeFallecimientoIsNull(Integer anioDeFallecimiento);


}
