package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Gol;
import com.sistema.torneos.app.service.GolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goles")
public class GolController {

    private final GolService service;

    public GolController(GolService service) {
        this.service = service;
    }

    @GetMapping
    public List<Gol> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Gol getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Gol> create(@RequestBody Gol gol) {
        return ResponseEntity.ok(service.create(gol));
    }

    @PutMapping("/{id}")
    public Gol update(@PathVariable("id") Long id, @RequestBody Gol gol) {
        return service.update(id, gol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
