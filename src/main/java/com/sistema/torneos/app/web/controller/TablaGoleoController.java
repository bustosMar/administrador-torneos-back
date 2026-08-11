package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.service.TablaGoleoService;
import com.sistema.torneos.app.web.model.response.TablaGoleoResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tabla-goleo")
@RequiredArgsConstructor
public class TablaGoleoController {

    private final TablaGoleoService tablaGoleoService;


    @GetMapping
    public ResponseEntity<TablaGoleoResponse> obtenerTablaGoleo(

            @RequestParam Long torneoId,

            @RequestParam Long categoriaId

    ) {

        TablaGoleoResponse resultado =
                tablaGoleoService.calcularTablaGoleo(
                        torneoId,
                        categoriaId
                );


        return ResponseEntity.ok(
                resultado
        );
    }
}