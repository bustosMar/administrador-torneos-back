package com.sistema.torneos.app.web.model.response;

import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Getter
@Setter
public class JornadaResponse {

    private Long idJornada;

    private Integer numeroJornada;

    private String estado;

    private Long idTorneo;

    private String torneo;

    private Long idGrupo;

    private String grupo;

    private LocalDate fechaProgramada;

    /**
     * Partidos de la jornada
     */
    private List<PartidoResponse> partidos;
}
