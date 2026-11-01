
package com.gerdoc.escuela.service.impl;

import com.gerdoc.escuela.model.ProfesorMateria;
import com.gerdoc.escuela.model.ProfesorMateriaId;
import com.gerdoc.escuela.repository.ProfesorMateriaRepository;
import com.gerdoc.escuela.service.ProfesorMateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfesorMateriaServiceImpl
        implements ProfesorMateriaService {

    private final ProfesorMateriaRepository repository;

    @Override
    public List<ProfesorMateria> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ProfesorMateria> findById(ProfesorMateriaId id) {
        return repository.findById(id);
    }

    @Override
    public ProfesorMateria save(ProfesorMateria relacion) {
        return repository.save(relacion);
    }

    @Override
    public boolean existsById(ProfesorMateriaId id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(ProfesorMateriaId id) {
        repository.deleteById(id);
    }
}
