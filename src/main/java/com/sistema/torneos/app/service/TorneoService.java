package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Torneo;

import java.util.List;

public interface TorneoService {
    List<Torneo> findAll();
    Torneo findById(Long id);
    Torneo create(Torneo torneo);
    Torneo update(Long id, Torneo torneo);
    void delete(Long id);
}
