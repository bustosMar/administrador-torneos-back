package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.repository.MunicipioEstadoRepository;
import com.sistema.torneos.app.web.model.MunicipioModel;
import com.sistema.torneos.app.web.model.mapper.MunicipioMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MunicipioController {

    private final MunicipioEstadoRepository municipioEstadoRepository;

    public MunicipioController(MunicipioEstadoRepository municipioEstadoRepository) {
        this.municipioEstadoRepository = municipioEstadoRepository;
    }

    @GetMapping("/municipios/estado/{estadoId}")
    public List<MunicipioModel> getMunicipiosByEstado(@PathVariable Long estadoId) {
        return municipioEstadoRepository.findByEstadoId(estadoId)
                .stream()
                .map(municipioEstado -> MunicipioMapper.INSTANCE.toModel(municipioEstado.getMunicipio()))
                .toList();
    }

    @GetMapping("/municipio-estado/find")
    public ResponseEntity<Long> findMunicipioEstadoId(
            @RequestParam Long municipioId,
            @RequestParam Long estadoId) {

        return municipioEstadoRepository.findByMunicipioIdAndEstadoId(municipioId, estadoId)
                .map(municipioEstado -> ResponseEntity.ok(municipioEstado.getId()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
