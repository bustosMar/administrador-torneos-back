package com.sistema.torneos.app.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SancionModel {

    private Long id;
    private Long partido;
    private Long jugador;
    private Long equipoTorneo;
    private Integer minuto;
    private String tipo;
    private String observacion;
}
