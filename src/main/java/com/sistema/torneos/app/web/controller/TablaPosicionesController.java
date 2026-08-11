package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.web.model.response.TablasTorneoResponse;
import com.sistema.torneos.app.web.model.response.TablasPosicionResponse;
import com.sistema.torneos.app.service.TablaPosicionesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tabla-posiciones")
@RequiredArgsConstructor
public class TablaPosicionesController {

    private final TablaPosicionesService tablaPosicionesService;

    @GetMapping
    public ResponseEntity<TablasTorneoResponse> obtenerTablaPosiciones(
            @RequestParam Long torneoId,
            @RequestParam Long categoriaId
    ) {

    	TablasTorneoResponse resultado =
                tablaPosicionesService.calcularTablas(
                        torneoId,
                        categoriaId
                );

        return ResponseEntity.ok(resultado);
    }
}