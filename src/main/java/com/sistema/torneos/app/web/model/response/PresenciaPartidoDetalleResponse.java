package com.sistema.torneos.app.web.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresenciaPartidoDetalleResponse {

	private Long idPartido;
	private Integer numeroJornada;
	private String torneo;
	private String municipioEstado;
	private String fecha;
	private String hora;
	private Long idEquipoLocal;
	private String equipoLocal;
	private Long idEquipoVisitante;
	private String equipoVisitante;
	private List<JugadorPartidoResponse> jugadoresLocal;
	private List<JugadorPartidoResponse> jugadoresVisitante;
	private List<JugadorPartidoResponse> presenciasRegistradas;
}