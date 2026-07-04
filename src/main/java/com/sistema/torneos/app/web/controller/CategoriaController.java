package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.service.CategoriaService;
import com.sistema.torneos.app.web.model.CategoriaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaModel> getAll() {
        return categoriaService.findAll();
    }

    @GetMapping("/activas")
    public List<CategoriaModel> getAllActivas() {
        return categoriaService.findAllActivas();
    }

    @GetMapping("/{id}")
    public CategoriaModel getById(@PathVariable Long id) {
        return categoriaService.findById(id);
    }

    @PostMapping
    public CategoriaModel create(@RequestBody CategoriaModel categoria) {
        return categoriaService.create(categoria);
    }

    @PutMapping("/{id}")
    public CategoriaModel update(@PathVariable Long id, @RequestBody CategoriaModel categoria) {
        return categoriaService.update(id, categoria);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoriaService.delete(id);
    }
}
