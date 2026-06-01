package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.Arbitro;
import com.sistema.torneos.app.service.ArbitroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arbitros")
public class ArbitroController {

    private final ArbitroService arbitroService;

    public ArbitroController(ArbitroService arbitroService) {
        this.arbitroService = arbitroService;
    }

    @GetMapping
    public List<Arbitro> getAll() {
        return arbitroService.findAll();
    }

    @GetMapping("/{id}")
    public Arbitro getById(@PathVariable Long id) {
        return arbitroService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Arbitro> create(@RequestBody Arbitro arbitro) {
        return ResponseEntity.ok(arbitroService.create(arbitro));
    }

    @PutMapping("/{id}")
    public Arbitro update(@PathVariable Long id, @RequestBody Arbitro arbitro) {
        return arbitroService.update(id, arbitro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        arbitroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
