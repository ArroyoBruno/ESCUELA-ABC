
package com.gerdoc.escuela.service.impl;

import com.gerdoc.escuela.model.Materia;
import com.gerdoc.escuela.repository.MateriaRepository;
import com.gerdoc.escuela.service.MateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository materiaRepository;

    @Override
    public List<Materia> findAll() {
        return materiaRepository.findAll();
    }

    @Override
    public Optional<Materia> findById(Long id) {
        return materiaRepository.findById(id);
    }

    @Override
    public Materia save(Materia materia) {
        return materiaRepository.save(materia);
    }

    @Override
    public boolean existsById(Long id) {
        return materiaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        materiaRepository.deleteById(id);
    }
}
