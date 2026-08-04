package com.sistema.torneos.app.web.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JugadorPartidoResponse {

	private Long idJugador;
	private String nombreCompleto;
	private String foto;
	private Long idEquipo;
	private String equipo;
	private boolean presente;
}