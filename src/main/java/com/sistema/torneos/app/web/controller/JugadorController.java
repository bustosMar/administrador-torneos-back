package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.service.JugadorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jugadores")
public class JugadorController {

    @Autowired
    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public List<Jugador> getAll() {
        return jugadorService.findAll();
    }

    @GetMapping("/{id}")
    public Jugador getById(@PathVariable("id") Long id) {
        return jugadorService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Jugador> create(@RequestBody Jugador jugador) {
        return ResponseEntity.ok(jugadorService.create(jugador));
    }

    @PutMapping("/{id}")
    public Jugador update(@PathVariable("id") Long id, @RequestBody Jugador jugador) {
        return jugadorService.update(id, jugador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        jugadorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
