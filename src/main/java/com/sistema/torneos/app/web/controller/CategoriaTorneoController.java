package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.service.CategoriaTorneoService;
import com.sistema.torneos.app.util.SearchUtil;
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
    public List<CategoriaTorneoModel> getAll(@RequestParam(required = false) Long torneoId) {
        if (torneoId != null) {
            return categoriaTorneoService.findByTorneoId(torneoId);
        }
        return categoriaTorneoService.findAll();
    }

    @GetMapping("/search")
    public List<CategoriaTorneoModel> search(@RequestParam("q") String query) {
        return SearchUtil.search(categoriaTorneoService.findAll(), query);
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

