package com.gerdoc.escuela.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PROFESOR_MATERIA")
public class ProfesorMateria
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PROFESOR_ID")
    private Profesor profesor;

    @ManyToOne
    @JoinColumn(name = "MATERIA_ID")
    private Materia materia;
}
