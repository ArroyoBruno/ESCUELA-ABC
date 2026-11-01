package com.gerdoc.escuela.controller;

import com.gerdoc.escuela.model.Alumno;
import com.gerdoc.escuela.service.AlumnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag( name = "Alumnos", description = "Operaciones CRUD para la administración de alumnos")
@RestController
@RequestMapping("/api/alumnos")
@RequiredArgsConstructor
public class AlumnoController
{
    private final AlumnoService alumnoService;

    @Operation(summary = "Consultar todos los alumnos", description = "Obtiene la lista completa de alumnos registrados")
    @GetMapping
    public ResponseEntity<List<Alumno>> findAll()
    {
        List<Alumno> alumnos = alumnoService.findAll( );
        return ResponseEntity.ok(alumnos);
    }

    @Operation(summary = "Consultar un alumno por ID", description = "Obtiene un alumno por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> findById(@PathVariable Long id)
    {
        return alumnoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo alumno", description = "Crea un nuevo alumno en el sistema")
    @PostMapping
    public ResponseEntity<Alumno> create(@RequestBody Alumno alumno)
    {
        alumno.setId(null);
        Alumno nuevoAlumno = alumnoService.save(alumno);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoAlumno);
    }

    @Operation(summary = "Actualizar un alumno existente", description = "Actualiza los datos de un alumno existente en el sistema")
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> update(@PathVariable Long id, @RequestBody Alumno alumno)
    {
        return alumnoService.findById(id)
                .map(alumnoExistente -> {
                    alumnoExistente.setNombre(alumno.getNombre());
                    alumnoExistente.setEdad(alumno.getEdad());
                    alumnoExistente.setPromedio(alumno.getPromedio());
                    Alumno actualizado = alumnoService.save(alumnoExistente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un alumno por ID", description = "Elimina un alumno del sistema por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        if (!alumnoService.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }
        alumnoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
