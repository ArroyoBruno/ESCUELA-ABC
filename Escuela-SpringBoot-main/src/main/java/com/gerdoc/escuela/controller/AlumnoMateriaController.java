
package com.gerdoc.escuela.controller;

import com.gerdoc.escuela.model.AlumnoMateria;
import com.gerdoc.escuela.model.Alumno;
import com.gerdoc.escuela.model.Materia;
import com.gerdoc.escuela.repository.AlumnoRepository;
import com.gerdoc.escuela.repository.MateriaRepository;
import com.gerdoc.escuela.service.AlumnoMateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Alumno Materia",
        description = "Relación entre alumnos, materias y calificaciones")
@RestController
@RequestMapping("/api/alumno-materia")
@RequiredArgsConstructor
public class AlumnoMateriaController {

    private final AlumnoMateriaService service;
    private final AlumnoRepository alumnoRepository;
    private final MateriaRepository materiaRepository;

    @Operation(summary = "Consultar todas las relaciones alumno-materia")
    @GetMapping
    public ResponseEntity<List<AlumnoMateria>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Consultar una relación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoMateria> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar una materia para un alumno")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RelacionRequest request) {

        var alumno = alumnoRepository.findById(request.alumnoId());
        var materia = materiaRepository.findById(request.materiaId());

        if (alumno.isEmpty() || materia.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El alumno o la materia no existen.");
        }

        AlumnoMateria relacion = new AlumnoMateria();
        relacion.setAlumno(alumno.get());
        relacion.setMateria(materia.get());
        relacion.setCalificacion(request.calificacion());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(relacion));
    }

    @Operation(summary = "Actualizar una relación y su calificación")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody RelacionRequest request) {

        var existente = service.findById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var alumno = alumnoRepository.findById(request.alumnoId());
        var materia = materiaRepository.findById(request.materiaId());

        if (alumno.isEmpty() || materia.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El alumno o la materia no existen.");
        }

        AlumnoMateria relacion = existente.get();
        relacion.setAlumno(alumno.get());
        relacion.setMateria(materia.get());
        relacion.setCalificacion(request.calificacion());

        return ResponseEntity.ok(service.save(relacion));
    }

    @Operation(summary = "Eliminar una relación por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record RelacionRequest(
            Long alumnoId,
            Long materiaId,
            java.math.BigDecimal calificacion) {
    }
}
