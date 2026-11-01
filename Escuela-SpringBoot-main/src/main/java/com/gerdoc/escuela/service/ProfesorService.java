
package com.gerdoc.escuela.service;

import com.gerdoc.escuela.model.Profesor;
import java.util.List;
import java.util.Optional;

public interface ProfesorService {

    List<Profesor> findAll();

    Optional<Profesor> findById(Long id);

    Profesor save(Profesor profesor);

    boolean existsById(Long id);

    void deleteById(Long id);
}
