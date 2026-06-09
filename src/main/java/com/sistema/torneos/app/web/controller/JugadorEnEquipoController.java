package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.web.model.JugadorEnEquipoModel;
import com.sistema.torneos.app.service.JugadorEnEquipoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jugadores-en-equipo")
public class JugadorEnEquipoController {

    private final JugadorEnEquipoService service;

    public JugadorEnEquipoController(JugadorEnEquipoService service) {
        this.service = service;
    }

    @GetMapping
    public List<JugadorEnEquipoModel> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public JugadorEnEquipoModel getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<JugadorEnEquipoModel> create(@RequestBody JugadorEnEquipoModel jugadorEnEquipo) {
        return ResponseEntity.ok(service.create(jugadorEnEquipo));
    }

    @PutMapping("/{id}")
    public JugadorEnEquipoModel update(
            @PathVariable("id") Long id,
            @RequestBody JugadorEnEquipoModel jugadorEnEquipo) {
        return service.update(id, jugadorEnEquipo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
