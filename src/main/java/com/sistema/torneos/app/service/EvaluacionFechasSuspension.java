package com.sistema.torneos.app.service;

import java.time.LocalDate;

public record EvaluacionFechasSuspension(
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String motivo) {
}
