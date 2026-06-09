package com.sistema.torneos.app.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JugadorEnEquipoModel {

    private Long id;

    private Long jugador;
    private String jugadorNombre;
    private String jugadorApellido;

    private Long equipo;
    private String equipoNombre;
    
    private Long torneo;
    private String torneoNombre;
    private boolean activo;
}