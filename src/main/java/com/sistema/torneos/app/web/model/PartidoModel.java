package com.sistema.torneos.app.web.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.*;

@Getter
@Setter
public class PartidoModel {

	private Long grupo;
    private String grupoNombre;
    private Long arbitro;
    private String arbitroNombre;
    private Long jornada;
    private Integer numeroJornada;
    private Long equipoLocal;
    private String equipoLocalNombre;
    private Long equipoVisitante;
    private String equipoVisitanteNombre;
    private String hora;
    private Boolean jugado;
    private LocalDate fecha;
    private Long id;
   
}
