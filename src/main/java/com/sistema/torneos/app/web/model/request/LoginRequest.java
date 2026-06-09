package com.sistema.torneos.app.web.model.request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String nombreUsuario;

    @NotBlank
    private String password;
}
