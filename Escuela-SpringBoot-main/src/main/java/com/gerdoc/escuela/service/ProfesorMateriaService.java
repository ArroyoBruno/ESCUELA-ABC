
package com.gerdoc.escuela.service;

import com.gerdoc.escuela.model.ProfesorMateria;
import com.gerdoc.escuela.model.ProfesorMateriaId;
import java.util.List;
import java.util.Optional;

public interface ProfesorMateriaService {
    List<ProfesorMateria> findAll();
    Optional<ProfesorMateria> findById(ProfesorMateriaId id);
    ProfesorMateria save(ProfesorMateria relacion);
    boolean existsById(ProfesorMateriaId id);
    void deleteById(ProfesorMateriaId id);
}
