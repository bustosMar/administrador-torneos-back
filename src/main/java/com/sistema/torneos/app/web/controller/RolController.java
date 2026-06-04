package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Rol;
import com.sistema.torneos.app.service.RolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService service;

    public RolController(RolService service) {
        this.service = service;
    }

    @GetMapping
    public List<Rol> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Rol getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Rol> create(@RequestBody Rol rol) {
        return ResponseEntity.ok(service.create(rol));
    }

    @PutMapping("/{id}")
    public Rol update(@PathVariable("id") Long id, @RequestBody Rol rol) {
        return service.update(id, rol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
