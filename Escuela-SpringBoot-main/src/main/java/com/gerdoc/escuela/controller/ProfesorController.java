
package com.gerdoc.escuela.controller;

import com.gerdoc.escuela.model.Profesor;
import com.gerdoc.escuela.service.ProfesorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Profesores", description = "Operaciones CRUD para la administración de profesores")
@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @Operation(summary = "Consultar todos los profesores")
    @GetMapping
    public ResponseEntity<List<Profesor>> findAll() {
        return ResponseEntity.ok(profesorService.findAll());
    }

    @Operation(summary = "Consultar un profesor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Profesor> findById(@PathVariable Long id) {
        return profesorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar un profesor")
    @PostMapping
    public ResponseEntity<Profesor> create(@RequestBody Profesor profesor) {
        profesor.setId(null);
        Profesor nuevo = profesorService.save(profesor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Actualizar un profesor")
    @PutMapping("/{id}")
    public ResponseEntity<Profesor> update(
            @PathVariable Long id,
            @RequestBody Profesor profesor) {

        return profesorService.findById(id)
                .map(existente -> {
                    existente.setNombre(profesor.getNombre());
                    existente.setEspecialidad(profesor.getEspecialidad());

                    Profesor actualizado = profesorService.save(existente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un profesor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!profesorService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        profesorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
