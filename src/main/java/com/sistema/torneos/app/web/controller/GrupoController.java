package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Grupo;
import com.sistema.torneos.app.service.GrupoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {
    
    @Autowired
    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public List<Grupo> getAll() {
        return grupoService.findAll();
    }

    @GetMapping("/{id}")
    public Grupo getById(@PathVariable Long id) {
        return grupoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Grupo> create(@RequestBody Grupo grupo) {
        return ResponseEntity.ok(grupoService.create(grupo));
    }

    @PutMapping("/{id}")
    public Grupo update(@PathVariable Long id, @RequestBody Grupo grupo) {
        return grupoService.update(id, grupo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        grupoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
