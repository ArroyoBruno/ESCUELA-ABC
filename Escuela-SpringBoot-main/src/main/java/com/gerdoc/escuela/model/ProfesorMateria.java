
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
@IdClass(ProfesorMateriaId.class)
public class ProfesorMateria {

    @Id
    @ManyToOne
    @JoinColumn(name = "PROFESOR_ID")
    private Profesor profesor;

    @Id
    @ManyToOne
    @JoinColumn(name = "MATERIA_ID")
    private Materia materia;
}

