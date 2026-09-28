package com.sistema.torneos.app.web.model.response;

import java.time.LocalDate;

public record PartidoPresenciaJugadorResponse(
        Long idPartido,
        LocalDate fecha,
        String hora,
        Integer numeroJornada,
        String equipoLocal,
        String equipoVisitante,
        String observaciones) {
}