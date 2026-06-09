package com.sistema.torneos.app.web.model;

import lombok.*;

@Getter
@Setter
public class EquipoEnTorneoModel {
   
	private Long id;

    private Long equipo;
    private String equipoNombre;

    private Long torneo;
    private String torneoNombre;

    private Long grupo;
    private String grupoNombre;
    
}
