package com.sistema.torneos.app.web.model.response;

import lombok.*;

@Getter
@Setter
public class EquipoEnTorneoResponse {
   
    private Long id;
    private String equipo;
    private String torneo;
    private String grupo;
    
}
