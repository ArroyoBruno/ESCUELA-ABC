
package com.gerdoc.escuela.controller;

import com.gerdoc.escuela.model.Profesor;
import com.gerdoc.escuela.model.Materia;
import com.gerdoc.escuela.model.ProfesorMateria;
import com.gerdoc.escuela.model.ProfesorMateriaId;
import com.gerdoc.escuela.repository.ProfesorRepository;
import com.gerdoc.escuela.repository.MateriaRepository;
import com.gerdoc.escuela.service.ProfesorMateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Profesor Materia",
        description = "Relación entre profesores y materias")
@RestController
@RequestMapping("/api/profesor-materia")
@RequiredArgsConstructor
public class ProfesorMateriaController {

    private final ProfesorMateriaService service;
    private final ProfesorRepository profesorRepository;
    private final MateriaRepository materiaRepository;

    @Operation(summary = "Consultar todas las relaciones")
    @GetMapping
    public ResponseEntity<List<ProfesorMateria>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Consultar una relación por IDs")
    @GetMapping("/{profesorId}/{materiaId}")
    public ResponseEntity<ProfesorMateria> findById(
            @PathVariable Long profesorId,
            @PathVariable Long materiaId) {

        ProfesorMateriaId id =
                new ProfesorMateriaId(profesorId, materiaId);

        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Asignar una materia a un profesor")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RelacionRequest request) {
        var profesor = profesorRepository.findById(request.profesorId());
        var materia = materiaRepository.findById(request.materiaId());

        if (profesor.isEmpty() || materia.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El profesor o la materia no existen.");
        }

        ProfesorMateria relacion = new ProfesorMateria();
        relacion.setProfesor(profesor.get());
        relacion.setMateria(materia.get());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(relacion));
    }

    @Operation(summary = "Actualizar una relación")
    @PutMapping("/{profesorId}/{materiaId}")
    public ResponseEntity<?> update(
            @PathVariable Long profesorId,
            @PathVariable Long materiaId,
            @RequestBody RelacionRequest request) {

        ProfesorMateriaId id =
                new ProfesorMateriaId(profesorId, materiaId);

        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var nuevoProfesor = profesorRepository.findById(request.profesorId());
        var nuevaMateria = materiaRepository.findById(request.materiaId());

        if (nuevoProfesor.isEmpty() || nuevaMateria.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El profesor o la materia no existen.");
        }

        service.deleteById(id);

        ProfesorMateria nuevaRelacion = new ProfesorMateria();
        nuevaRelacion.setProfesor(nuevoProfesor.get());
        nuevaRelacion.setMateria(nuevaMateria.get());

        return ResponseEntity.ok(service.save(nuevaRelacion));
    }

    @Operation(summary = "Eliminar una relación")
    @DeleteMapping("/{profesorId}/{materiaId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long profesorId,
            @PathVariable Long materiaId) {

        ProfesorMateriaId id =
                new ProfesorMateriaId(profesorId, materiaId);

        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record RelacionRequest(Long profesorId, Long materiaId) {}
}
