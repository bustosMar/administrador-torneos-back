package com.sistema.torneos.app.web.model;

import lombok.*;

@Getter
@Setter
public class PresenciaPartidoModel {

    private Long id;
    private Long partido;
    private Long jugador;
    private String observaciones;
}
