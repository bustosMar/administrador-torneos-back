package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.web.model.EstadoModel;
import com.sistema.torneos.app.domain.repository.EstadoRepository;
import com.sistema.torneos.app.web.model.mapper.EstadoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
public class EstadoController {

    private final EstadoRepository estadoRepository;

    public EstadoController(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    @GetMapping
    public List<EstadoModel> getAll() {
        return EstadoMapper.INSTANCE.toModel(estadoRepository.findAll());
    }

    @GetMapping("/{id}")
    public EstadoModel getById(@PathVariable Long id) {
        return estadoRepository.findById(id)
            .map(EstadoMapper.INSTANCE::toModel)
            .orElse(null);
    }
}
