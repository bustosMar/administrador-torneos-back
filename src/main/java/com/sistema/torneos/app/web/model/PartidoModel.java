package com.sistema.torneos.app.web.model;

import java.time.OffsetDateTime;
import lombok.*;

@Getter
@Setter
public class PartidoModel {

    private Long id;
    private Long torneo;
    private Long grupo;
    private Long arbitro;
    private OffsetDateTime fechaHora;
    private Long equipo;
}
