package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.service.CategoriaTorneoService;
import com.sistema.torneos.app.web.model.CategoriaTorneoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categoria-torneo")
public class CategoriaTorneoController {

    @Autowired
    private CategoriaTorneoService categoriaTorneoService;

    @GetMapping
    public List<CategoriaTorneoModel> getAll() {
        return categoriaTorneoService.findAll();
    }

    @GetMapping("/{id}")
    public CategoriaTorneoModel getById(@PathVariable Long id) {
        return categoriaTorneoService.findById(id);
    }

    @PostMapping
    public CategoriaTorneoModel create(@RequestBody CategoriaTorneoModel categoriaTorneo) {
        return categoriaTorneoService.create(categoriaTorneo);
    }

    @PutMapping("/{id}")
    public CategoriaTorneoModel update(@PathVariable Long id, @RequestBody CategoriaTorneoModel categoriaTorneo) {
        return categoriaTorneoService.update(id, categoriaTorneo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoriaTorneoService.delete(id);
    }
}

