package com.sistema.torneos.app.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.sistema.torneos.app.web.model.SancionModel;
import com.sistema.torneos.app.web.model.SuspensionModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JugadorSancionesSuspensionesResponse {

    private Long jugadorId;

    private String jugador;

    private Long equipoId;

    private String equipo;

    private List<SancionModel> sanciones;

    private List<SuspensionModel> suspensiones;

}