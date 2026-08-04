package com.sistema.torneos.app.web.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroPresenciaHuellaResponse {

	private boolean registrada;
	private String mensaje;
	private JugadorPartidoResponse jugador;
	private List<JugadorPartidoResponse> presenciasRegistradas;
}