package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Rol;

import java.util.List;

public interface RolService {
    List<Rol> findAll();
    Rol findById(Long id);
    Rol create(Rol rol);
    Rol update(Long id, Rol rol);
    void delete(Long id);
}
