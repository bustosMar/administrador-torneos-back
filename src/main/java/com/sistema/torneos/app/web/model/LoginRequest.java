package com.sistema.torneos.app.web.model;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    private String nombreUsuario;

    @NotBlank
    private String password;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
