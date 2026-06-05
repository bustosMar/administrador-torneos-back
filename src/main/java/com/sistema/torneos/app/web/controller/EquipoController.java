package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.web.model.EquipoModel;
import com.sistema.torneos.app.service.EquipoService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {
    
    @Autowired
    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public List<EquipoModel> getAll() {
        return equipoService.findAll();
    }

    @GetMapping("/{id}")
    public EquipoModel getById(@PathVariable("id") Long id) {
        return equipoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<EquipoModel> create(@RequestBody EquipoModel equipo) {
        return ResponseEntity.ok(equipoService.create(equipo));
    }

    @PutMapping("/{id}")
    public EquipoModel update(@PathVariable("id") Long id, @RequestBody EquipoModel equipo) {
        return equipoService.update(id, equipo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        equipoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
