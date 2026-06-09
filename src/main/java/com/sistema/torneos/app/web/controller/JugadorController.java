package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.service.JugadorService;
import com.sistema.torneos.app.web.model.JugadorModel;

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
    public List<JugadorModel> getAll() {
        return jugadorService.findAll();
    }

    @GetMapping("/{id}")
    public JugadorModel getById(@PathVariable("id") Long id) {
        return jugadorService.findById(id);
    }

    @PostMapping
    public ResponseEntity<JugadorModel> create(@RequestBody JugadorModel jugador) {
        return ResponseEntity.ok(jugadorService.create(jugador));
    }

    @PutMapping("/{id}")
    public JugadorModel update(@PathVariable("id") Long id, @RequestBody JugadorModel jugador) {
        return jugadorService.update(id, jugador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        jugadorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
