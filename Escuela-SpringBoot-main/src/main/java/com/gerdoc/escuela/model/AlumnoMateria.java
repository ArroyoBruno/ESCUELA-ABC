
package com.gerdoc.escuela.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ALUMNO_MATERIA")
public class AlumnoMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ALUMNO_ID", nullable = false)
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "MATERIA_ID", nullable = false)
    private Materia materia;

    @Column(name = "CALIFICACION")
    private BigDecimal calificacion;
}
