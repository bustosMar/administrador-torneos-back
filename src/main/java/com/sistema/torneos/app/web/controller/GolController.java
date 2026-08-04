package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.service.GolService;
import com.sistema.torneos.app.web.model.GolModel;

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
    public List<GolModel> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public GolModel getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<GolModel> create(@RequestBody GolModel gol) {
        return ResponseEntity.ok(service.create(gol));
    }

    @PutMapping("/{id}")
    public GolModel update(@PathVariable("id") Long id, @RequestBody GolModel gol) {
        return service.update(id, gol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
