package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.service.PartidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    @Autowired
    private final PartidoService partidoService;

    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @GetMapping
    public List<Partido> getAll() {
        return partidoService.findAll();
    }

    @GetMapping("/{id}")
    public Partido getById(@PathVariable Long id) {
        return partidoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Partido> create(@RequestBody Partido partido) {
        return ResponseEntity.ok(partidoService.create(partido));
    }

    @PutMapping("/{id}")
    public Partido update(@PathVariable Long id, @RequestBody Partido partido) {
        return partidoService.update(id, partido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        partidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
