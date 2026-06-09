package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.web.model.UsuarioModel;
import com.sistema.torneos.app.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioModel> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public UsuarioModel getById(@PathVariable("id") Long id) {
        return usuarioService.findById(id);
    }

    @PostMapping
    public ResponseEntity<UsuarioModel> create(@RequestBody UsuarioModel usuario) {
        return ResponseEntity.ok(usuarioService.create(usuario));
    }

    @PutMapping("/{id}")
    public UsuarioModel update(@PathVariable("id") Long id, @RequestBody UsuarioModel usuario) {
        return usuarioService.update(id, usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
