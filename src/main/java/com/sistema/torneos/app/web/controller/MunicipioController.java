package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.web.model.MunicipioModel;
import com.sistema.torneos.app.domain.repository.MunicipioRepository;
import com.sistema.torneos.app.domain.repository.MunicipioEstadoRepository;
import com.sistema.torneos.app.web.model.mapper.MunicipioMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/municipios")
public class MunicipioController {

    private final MunicipioRepository municipioRepository;
    private final MunicipioEstadoRepository municipioEstadoRepository;

    public MunicipioController(MunicipioRepository municipioRepository, MunicipioEstadoRepository municipioEstadoRepository) {
        this.municipioRepository = municipioRepository;
        this.municipioEstadoRepository = municipioEstadoRepository;
    }

    @GetMapping
    public List<MunicipioModel> getAll() {
        return MunicipioMapper.INSTANCE.toModel(municipioRepository.findAll());
    }

    @GetMapping("/estado/{estadoId}")
    public List<MunicipioModel> getMunicipiosByEstado(@PathVariable Long estadoId) {
        return municipioEstadoRepository.findByEstadoId(estadoId)
            .stream()
            .map(me -> MunicipioMapper.INSTANCE.toModel(me.getMunicipio()))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MunicipioModel getById(@PathVariable Long id) {
        return municipioRepository.findById(id)
            .map(MunicipioMapper.INSTANCE::toModel)
            .orElse(null);
    }
}
