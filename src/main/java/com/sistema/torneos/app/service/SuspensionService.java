package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Suspension;

import java.util.List;

public interface SuspensionService {
    List<Suspension> findAll();
    Suspension findById(Long id);
    Suspension create(Suspension suspension);
    Suspension update(Long id, Suspension suspension);
    void delete(Long id);
}
