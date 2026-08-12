package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.service.SancionService;
import com.sistema.torneos.app.web.model.SancionModel;
import com.sistema.torneos.app.web.model.response.JugadoresSancionesSuspensionesResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sanciones")
public class SancionController {

    private final SancionService service;

    public SancionController(SancionService service) {
        this.service = service;
    }

    // =========================================================
    // OBTENER TODAS LAS SANCIONES
    // =========================================================

    @GetMapping
    public List<SancionModel> getAll() {

        return service.findAll();
    }

    // =========================================================
    // OBTENER SANCION POR ID
    // =========================================================

    @GetMapping("/{id}")
    public SancionModel getById(
            @PathVariable("id") Long id
    ) {

        return service.findById(id);
    }

    // =========================================================
    // CREAR
    // =========================================================

    @PostMapping
    public ResponseEntity<SancionModel> create(
            @RequestBody SancionModel sancion
    ) {

        return ResponseEntity.ok(
                service.create(sancion)
        );
    }

    // =========================================================
    // ACTUALIZAR
    // =========================================================

    @PutMapping("/{id}")
    public SancionModel update(
            @PathVariable("id") Long id,
            @RequestBody SancionModel sancion
    ) {

        return service.update(
                id,
                sancion
        );
    }

    // =========================================================
    // ELIMINAR
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // BUSCAR JUGADORES
    //
    // Este endpoint recibe STRING.
    //
    // Ejemplos:
    //
    // /api/sanciones/buscar-jugadores?nombre=Mar
    //
    // /api/sanciones/buscar-jugadores?nombre=Bus
    //
    // /api/sanciones/buscar-jugadores?nombre=Mar%20Bus
    //
    // =========================================================

    @GetMapping("/buscar-jugadores")
    public ResponseEntity<List<Jugador>> buscarJugadores(
            @RequestParam("nombre") String nombre
    ) {

        return ResponseEntity.ok(
                service.buscarJugadores(nombre)
        );
    }

    // =========================================================
    // BUSCAR SANCIONES Y SUSPENSIONES
    //
    // Este endpoint recibe LONG.
    //
    // Ejemplo:
    //
    // /api/sanciones/buscar-por-jugador-id?jugadorId=76
    //
    // =========================================================

    @GetMapping("/buscar-por-jugador-id")
    public ResponseEntity<JugadoresSancionesSuspensionesResponse>
    buscarPorJugadorId(
            @RequestParam("jugadorId") Long jugadorId
    ) {

        return ResponseEntity.ok(
                service.buscarSancionesSuspensionesPorJugador(
                        jugadorId
                )
        );
    }
}