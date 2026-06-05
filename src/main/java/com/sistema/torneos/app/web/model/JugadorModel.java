package com.sistema.torneos.app.web.model;

import java.util.Date;
import lombok.*;

@Getter
@Setter
public class JugadorModel {


    private Long id;
    private String nombre;
    private Date fechaNacimiento;
    private Integer numeroCamisa;
    private Long equipo;
    private byte[] foto;
    private byte[] huella;
    
}
