package com.sistema.torneos.app.web.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartidoResponse {

    private Long idPartido;

    private Long idJornada;

    private Integer numeroJornada;

    private String estado;

    /**
     * Torneo (opcional si lo quieres mostrar en UI)
     */
    private Long idTorneo;
    private String torneo;

    /**
     * Grupo (si aplica)
     */
    private Long idGrupo;
    private String grupo;

    /**
     * Equipos
     */
    private Long idEquipoLocal;
    private String equipoLocal;

    private Long idEquipoVisitante;
    private String equipoVisitante;

    /**
     * Fecha y hora del partido
     */
    private String fecha;
    private String hora;

    /**
     * Resultado
     */
    private Integer golesLocal;
    private Integer golesVisitante;

    /**
     * Árbitro (opcional)
     */
    private Long idArbitro;
    private String arbitro;
}
