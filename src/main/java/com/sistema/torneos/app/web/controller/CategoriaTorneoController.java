package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.CategoriaTorneo;
import com.sistema.torneos.app.service.CategoriaTorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categoria-torneo")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CategoriaTorneoController {

    @Autowired
    private CategoriaTorneoService categoriaTorneoService;

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<List<CategoriaTorneo>> obtenerCategoriasDelTorneo(@PathVariable Long torneoId) {
        List<CategoriaTorneo> categorias = categoriaTorneoService.obtenerCategoriasDelTorneo(torneoId);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/torneo/{torneoId}/activas")
    public ResponseEntity<List<CategoriaTorneo>> obtenerCategoriasActivasDelTorneo(@PathVariable Long torneoId) {
        List<CategoriaTorneo> categorias = categoriaTorneoService.obtenerCategoriasActivasDelTorneo(torneoId);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaTorneo> obtenerCategoriaTorneoById(@PathVariable Long id) {
        Optional<CategoriaTorneo> categoriaTorneo = categoriaTorneoService.obtenerCategoriaTorneoById(id);
        return categoriaTorneo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/torneo/{torneoId}/categoria/{categoriaId}")
    public ResponseEntity<CategoriaTorneo> agregarCategoriaAlTorneo(
            @PathVariable Long torneoId,
            @PathVariable Long categoriaId,
            @RequestParam(required = false) Integer orden) {
        try {
            CategoriaTorneo categoriaTorneo = categoriaTorneoService
                    .agregarCategoriaAlTorneo(torneoId, categoriaId, orden);
            if (categoriaTorneo != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(categoriaTorneo);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaTorneo> actualizarCategoriaTorneo(
            @PathVariable Long id,
            @RequestBody CategoriaTorneo categoriaTorneo) {
        CategoriaTorneo actualizada = categoriaTorneoService.actualizarCategoriaTorneo(id, categoriaTorneo);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarCategoriaTorneo(@PathVariable Long id) {
        categoriaTorneoService.desactivarCategoriaTorneo(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoriaTorneo(@PathVariable Long id) {
        categoriaTorneoService.eliminarCategoriaTorneo(id);
        return ResponseEntity.noContent().build();
    }
}
