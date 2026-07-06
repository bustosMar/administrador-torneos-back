package com.sistema.torneos.app.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaModel {

    private Long id;
    
    private String nombre;
    
    private Integer edadMinima;  // nullable - sin restriccion minima si es null

    private Integer edadMaxima;  // nullable - sin restriccion maxima si es null
}
