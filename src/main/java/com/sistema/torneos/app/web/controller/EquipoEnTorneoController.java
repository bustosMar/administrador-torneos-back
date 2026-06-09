package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.web.model.EquipoEnTorneoModel;
import com.sistema.torneos.app.web.model.response.EquipoEnTorneoResponse;
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
    public List<EquipoEnTorneoModel> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EquipoEnTorneoModel getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<EquipoEnTorneoModel> create(@RequestBody EquipoEnTorneoModel equipoEnTorneo) {
        return ResponseEntity.ok(service.create(equipoEnTorneo));
    }

    @PutMapping("/{id}")
    public EquipoEnTorneoModel update(@PathVariable("id") Long id, @RequestBody EquipoEnTorneoModel equipoEnTorneo) {
        return service.update(id, equipoEnTorneo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
