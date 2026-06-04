package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Suspension;
import com.sistema.torneos.app.service.SuspensionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suspensiones")
public class SuspensionController {

    private final SuspensionService service;

    public SuspensionController(SuspensionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Suspension> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Suspension getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Suspension> create(@RequestBody Suspension suspension) {
        return ResponseEntity.ok(service.create(suspension));
    }

    @PutMapping("/{id}")
    public Suspension update(@PathVariable("id") Long id, @RequestBody Suspension suspension) {
        return service.update(id, suspension);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
