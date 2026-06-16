package com.sistema.torneos.app.web.model.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

@Getter
@Setter
public class PartidoRequest {
   
	private Long idTorneo;
	private Long idGrupo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha;
    private String hora;
    private Long idLocal;
   	private Long idVisitante;
}
