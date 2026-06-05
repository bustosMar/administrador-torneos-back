package com.sistema.torneos.app.web.model;

import lombok.*;

@Getter
@Setter
public class UsuarioModel {
    
    private Long id;
    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String password;
    private Long rol;
    private boolean administrador;
    
}
