
package com.gerdoc.escuela.repository;

import com.gerdoc.escuela.model.AlumnoMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoMateriaRepository
        extends JpaRepository<AlumnoMateria, Long> {
}
