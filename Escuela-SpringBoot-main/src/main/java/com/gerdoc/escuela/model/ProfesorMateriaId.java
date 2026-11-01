
package com.gerdoc.escuela.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorMateriaId implements Serializable {

    private Long profesor;
    private Long materia;
}
