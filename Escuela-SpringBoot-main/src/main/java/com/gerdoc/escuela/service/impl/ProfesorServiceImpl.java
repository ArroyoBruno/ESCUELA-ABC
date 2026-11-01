
package com.gerdoc.escuela.service.impl;

import com.gerdoc.escuela.model.Profesor;
import com.gerdoc.escuela.repository.ProfesorRepository;
import com.gerdoc.escuela.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository profesorRepository;

    @Override
    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    @Override
    public Optional<Profesor> findById(Long id) {
        return profesorRepository.findById(id);
    }

    @Override
    public Profesor save(Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    @Override
    public boolean existsById(Long id) {
        return profesorRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        profesorRepository.deleteById(id);
    }
}
