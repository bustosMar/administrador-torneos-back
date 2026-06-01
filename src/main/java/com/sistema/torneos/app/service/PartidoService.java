package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Partido;

import java.util.List;

public interface PartidoService {
    List<Partido> findAll();
    Partido findById(Long id);
    Partido create(Partido partido);
    Partido update(Long id, Partido partido);
    void delete(Long id);
}
