package com.sistema.torneos.app.web.controller;


import com.sistema.torneos.app.service.TorneoService;
import com.sistema.torneos.app.web.model.TorneoModel;

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
    public List<TorneoModel> getAll() {
        return torneoService.findAll();
    }

    @GetMapping("/{id}")
    public TorneoModel getById(@PathVariable("id") Long id) {
        return torneoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TorneoModel> create(@RequestBody TorneoModel torneo) {
        return ResponseEntity.ok(torneoService.create(torneo));
    }
    
    @PutMapping("/{id}")
    public TorneoModel update(@PathVariable("id") Long id,
                         @RequestBody TorneoModel torneo) {
        return torneoService.update(id, torneo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        torneoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
