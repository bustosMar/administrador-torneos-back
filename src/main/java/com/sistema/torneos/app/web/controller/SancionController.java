package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.service.SancionService;
import com.sistema.torneos.app.web.model.SancionModel;
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

    @GetMapping
    public List<SancionModel> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SancionModel getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<SancionModel> create(@RequestBody SancionModel sancion) {
        return ResponseEntity.ok(service.create(sancion));
    }

    @PutMapping("/{id}")
    public SancionModel update(@PathVariable("id") Long id, @RequestBody SancionModel sancion) {
        return service.update(id, sancion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
