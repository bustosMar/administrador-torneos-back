package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Grupo;

import java.util.List;

public interface GrupoService {
    List<Grupo> findAll();
    Grupo findById(Long id);
    Grupo create(Grupo grupo);
    Grupo update(Long id, Grupo grupo);
    void delete(Long id);
}
