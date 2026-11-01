package com.gerdoc.escuela.service;

import com.gerdoc.escuela.model.Materia;
import java.util.List;
import java.util.Optional;

public interface MateriaService {
    List<Materia> findAll();
    Optional<Materia> findById(Long id);
    Materia save(Materia materia);
    boolean existsById(Long id);
    void deleteById(Long id);
}
