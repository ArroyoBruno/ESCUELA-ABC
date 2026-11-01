package com.gerdoc.escuela.service;


import com.gerdoc.escuela.model.Alumno;

import java.util.List;
import java.util.Optional;

public interface AlumnoService
{
    // Consultar todos los alumnos
    List<Alumno> findAll();
    // Consultar un alumno por ID
    Optional<Alumno> findById(Long id);
    // Guardar o actualizar un alumno
    Alumno save(Alumno alumno);
    // Eliminar un alumno por ID
    void deleteById(Long id);
    // Verificar si existe un alumno
    boolean existsById(Long id);
}
