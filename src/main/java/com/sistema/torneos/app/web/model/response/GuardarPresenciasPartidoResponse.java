package com.sistema.torneos.app.web.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuardarPresenciasPartidoResponse {

	private String mensaje;
	private List<JugadorPartidoResponse> presenciasRegistradas;
}