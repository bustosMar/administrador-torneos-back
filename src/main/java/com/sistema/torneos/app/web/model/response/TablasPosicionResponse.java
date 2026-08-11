package com.sistema.torneos.app.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TablasPosicionResponse {
	
	private Integer posicion;

    private Long equipoId;
    private String equipo;

    private Long grupoId;
    private String grupo;

    private int partidosJugados;
    private int partidosGanados;
    private int partidosEmpatados;
    private int partidosPerdidos;

    private int golesFavor;
    private int golesContra;
    private int diferenciaGoles;

    private int puntos;

}
