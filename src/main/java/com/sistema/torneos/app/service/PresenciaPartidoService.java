package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.PresenciaPartido;

import java.util.List;

public interface PresenciaPartidoService {
    List<PresenciaPartido> findAll();
    PresenciaPartido findById(Long id);
    PresenciaPartido create(PresenciaPartido presenciaPartido);
    PresenciaPartido update(Long id, PresenciaPartido presenciaPartido);
    void delete(Long id);
}
