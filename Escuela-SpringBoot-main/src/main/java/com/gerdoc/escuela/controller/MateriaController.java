
package com.gerdoc.escuela.controller;

import com.gerdoc.escuela.model.Materia;
import com.gerdoc.escuela.service.MateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Materias", description = "Operaciones CRUD para la administración de materias")
@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    @Operation(summary = "Consultar todas las materias")
    @GetMapping
    public ResponseEntity<List<Materia>> findAll() {
        return ResponseEntity.ok(materiaService.findAll());
    }

    @Operation(summary = "Consultar una materia por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Materia> findById(@PathVariable Long id) {
        return materiaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar una materia")
    @PostMapping
    public ResponseEntity<Materia> create(@RequestBody Materia materia) {
        materia.setId(null);
        Materia nueva = materiaService.save(materia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Actualizar una materia")
    @PutMapping("/{id}")
    public ResponseEntity<Materia> update(
            @PathVariable Long id,
            @RequestBody Materia materia) {

        return materiaService.findById(id)
                .map(existente -> {
                    existente.setNombre(materia.getNombre());
                    existente.setCreditos(materia.getCreditos());
                    Materia actualizada = materiaService.save(existente);
                    return ResponseEntity.ok(actualizada);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una materia")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!materiaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        materiaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
