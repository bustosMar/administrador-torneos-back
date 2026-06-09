package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.web.model.ArbitroModel;
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
    public List<ArbitroModel> getAll() {
        return arbitroService.findAll();
    }

    @GetMapping("/{id}")
    public ArbitroModel getById(@PathVariable("id") Long id) {
        return arbitroService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ArbitroModel> create(@RequestBody ArbitroModel arbitro) {
        return ResponseEntity.ok(arbitroService.create(arbitro));
    }

    @PutMapping("/{id}")
    public ArbitroModel update(@PathVariable("id") Long id, @RequestBody ArbitroModel arbitro) {
        return arbitroService.update(id, arbitro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        arbitroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
