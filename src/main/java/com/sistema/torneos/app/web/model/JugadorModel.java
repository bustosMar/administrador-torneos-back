package com.sistema.torneos.app.web.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
public class JugadorModel {


    private Long id;
    private String nombre;
    private String apellido;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;
    private String foto;
    
}
