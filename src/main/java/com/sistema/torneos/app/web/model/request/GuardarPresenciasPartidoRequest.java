package com.sistema.torneos.app.web.model.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuardarPresenciasPartidoRequest {

	private List<Long> jugadores;
	private String observaciones;
}