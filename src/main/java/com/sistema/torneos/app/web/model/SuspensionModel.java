package com.sistema.torneos.app.web.model;

import java.util.Date;
import lombok.*;

@Getter
@Setter
public class SuspensionModel {
    

    private Long id;
    private Long jugador;
    private Date fechaInicio;
    private Date fechaFin;
    private String motivo;

}
