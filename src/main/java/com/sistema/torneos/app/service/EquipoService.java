package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Equipo;

import java.util.List;

public interface EquipoService {
    List<Equipo> findAll();
    Equipo findById(Long id);
    Equipo create(Equipo equipo);
    Equipo update(Long id, Equipo equipo);
    void delete(Long id);
}
