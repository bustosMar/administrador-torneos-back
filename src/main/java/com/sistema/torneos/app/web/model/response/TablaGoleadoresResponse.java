package com.sistema.torneos.app.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TablaGoleadoresResponse {

    private Long jugadorId;

    private String jugador;

    private Long equipoId;

    private String equipo;

    private int goles;
    
    private Integer posicion;
}
