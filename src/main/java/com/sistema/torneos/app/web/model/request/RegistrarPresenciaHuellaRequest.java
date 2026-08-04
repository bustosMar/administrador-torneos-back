package com.sistema.torneos.app.web.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarPresenciaHuellaRequest {

	private Long equipoId;
	private String huella;
	private String observaciones;
}