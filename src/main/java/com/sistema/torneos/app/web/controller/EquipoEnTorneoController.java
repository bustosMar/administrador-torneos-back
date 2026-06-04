package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.service.EquipoEnTorneoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos-en-torneo")
public class EquipoEnTorneoController {

    private final EquipoEnTorneoService service;

    public EquipoEnTorneoController(EquipoEnTorneoService service) {
        this.service = service;
    }

    @GetMapping
    public List<EquipoEnTorneo> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EquipoEnTorneo getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<EquipoEnTorneo> create(@RequestBody EquipoEnTorneo equipoEnTorneo) {
        return ResponseEntity.ok(service.create(equipoEnTorneo));
    }

    @PutMapping("/{id}")
    public EquipoEnTorneo update(@PathVariable("id") Long id, @RequestBody EquipoEnTorneo equipoEnTorneo) {
        return service.update(id, equipoEnTorneo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
