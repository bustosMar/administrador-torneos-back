package com.sistema.torneos.app.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaTorneoModel {

    private Long id;
    
    private Long torneo;
    private String torneoNombre;
    
    private Long categoria;
    private String categoriaNombre;
    
    private boolean activa;    
   
}
