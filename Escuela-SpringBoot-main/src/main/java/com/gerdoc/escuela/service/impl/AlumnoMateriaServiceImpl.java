
package com.gerdoc.escuela.service.impl;

import com.gerdoc.escuela.model.AlumnoMateria;
import com.gerdoc.escuela.repository.AlumnoMateriaRepository;
import com.gerdoc.escuela.service.AlumnoMateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlumnoMateriaServiceImpl implements AlumnoMateriaService {

    private final AlumnoMateriaRepository repository;

    @Override
    public List<AlumnoMateria> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<AlumnoMateria> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public AlumnoMateria save(AlumnoMateria relacion) {
        return repository.save(relacion);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
