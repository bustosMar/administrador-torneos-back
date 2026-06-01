package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;

import java.util.List;

public interface EquipoEnTorneoService {
    List<EquipoEnTorneo> findAll();
    EquipoEnTorneo findById(Long id);
    EquipoEnTorneo create(EquipoEnTorneo equipoEnTorneo);
    EquipoEnTorneo update(Long id, EquipoEnTorneo equipoEnTorneo);
    void delete(Long id);
}
