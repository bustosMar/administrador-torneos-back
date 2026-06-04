package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.service.TorneoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    @Autowired
    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @GetMapping
    public List<Torneo> getAll() {
        return torneoService.findAll();
    }

    @GetMapping("/{id}")
    public Torneo getById(@PathVariable("id") Long id) {
        return torneoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Torneo> create(@RequestBody Torneo torneo) {
        return ResponseEntity.ok(torneoService.create(torneo));
    }
    
    @PutMapping("/{id}")
    public Torneo update(@PathVariable("id") Long id,
                         @RequestBody Torneo torneo) {
        return torneoService.update(id, torneo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        torneoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
