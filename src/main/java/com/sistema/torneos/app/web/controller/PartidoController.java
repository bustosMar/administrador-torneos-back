package com.sistema.torneos.app.web.controller;


import com.sistema.torneos.app.service.PartidoService;
import com.sistema.torneos.app.web.model.PartidoModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    @Autowired
    private final PartidoService partidoService;

    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @GetMapping
    public List<PartidoModel> getAll() {
        return partidoService.findAll();
    }

    @GetMapping("/{id}")
    public PartidoModel getById(@PathVariable("id") Long id) {
        return partidoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<PartidoModel> create(@RequestBody PartidoModel partido) {
        return ResponseEntity.ok(partidoService.create(partido));
    }

    @PutMapping("/{id}")
    public PartidoModel update(@PathVariable("id") Long id, @RequestBody PartidoModel partido) {
        return partidoService.update(id, partido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        partidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/jornada")
    public ResponseEntity<Void> partidos(@RequestBody List<PartidoModel> partido) {
    	partidoService.createPartidos(partido);
    	return ResponseEntity.noContent().build();
    }
    
}
