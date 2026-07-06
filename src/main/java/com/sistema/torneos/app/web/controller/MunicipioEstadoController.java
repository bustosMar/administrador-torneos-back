package com.sistema.torneos.app.web.controller;

import com.sistema.torneos.app.domain.repository.MunicipioEstadoRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/municipio-estado")
public class MunicipioEstadoController {

    private final MunicipioEstadoRepository municipioEstadoRepository;

    public MunicipioEstadoController(MunicipioEstadoRepository municipioEstadoRepository) {
        this.municipioEstadoRepository = municipioEstadoRepository;
    }

    @GetMapping("/find")
    public Long getMunicipioEstadoId(@RequestParam Long municipioId, @RequestParam Long estadoId) {
        return municipioEstadoRepository.findByMunicipioIdAndEstadoId(municipioId, estadoId)
            .map(me -> me.getId())
            .orElse(null);
    }
}
