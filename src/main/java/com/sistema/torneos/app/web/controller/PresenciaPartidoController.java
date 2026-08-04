package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.entity.PresenciaPartido;
import com.sistema.torneos.app.service.PresenciaPartidoService;
import com.sistema.torneos.app.web.model.request.GuardarPresenciasPartidoRequest;
import com.sistema.torneos.app.web.model.request.RegistrarPresenciaHuellaRequest;
import com.sistema.torneos.app.web.model.response.GuardarPresenciasPartidoResponse;
import com.sistema.torneos.app.web.model.response.PresenciaPartidoDetalleResponse;
import com.sistema.torneos.app.web.model.response.RegistroPresenciaHuellaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/presencias")
public class PresenciaPartidoController {

    private final PresenciaPartidoService service;

    public PresenciaPartidoController(PresenciaPartidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<PresenciaPartido> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PresenciaPartido getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @GetMapping("/partido/{idPartido}")
    public PresenciaPartidoDetalleResponse getDetalleByPartido(@PathVariable Long idPartido) {
	return service.getDetalleByPartido(idPartido);
    }

    @PostMapping
    public ResponseEntity<PresenciaPartido> create(@RequestBody PresenciaPartido presenciaPartido) {
        return ResponseEntity.ok(service.create(presenciaPartido));
    }

    @PostMapping("/partido/{idPartido}/huella")
    public RegistroPresenciaHuellaResponse registrarPorHuella(
	    @PathVariable Long idPartido,
	    @RequestBody RegistrarPresenciaHuellaRequest request) {
	return service.registrarPorHuella(idPartido, request);
    }

    @PostMapping("/partido/{idPartido}/guardar")
    public GuardarPresenciasPartidoResponse guardarPresencias(
        @PathVariable Long idPartido,
        @RequestBody GuardarPresenciasPartidoRequest request) {
    return service.guardarPresencias(idPartido, request);
    }

    @PutMapping("/{id}")
    public PresenciaPartido update(@PathVariable("id") Long id, @RequestBody PresenciaPartido presenciaPartido) {
        return service.update(id, presenciaPartido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
