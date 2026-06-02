package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Rol;
import com.sistema.torneos.app.facade.RolFacade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {

    private final RolFacade rolFacade;

    @Autowired
    public RolService(RolFacade rolFacade) {
        this.rolFacade = rolFacade;
    }

    public List<Rol> findAll() {
        return rolFacade.findAll();
    }

    public Rol findById(Long id) {
        return rolFacade.findById(id);
    }

    public Rol create(Rol rol) {
        return rolFacade.create(rol);
    }

    public Rol update(Long id, Rol rol) {
        return rolFacade.update(id, rol);
    }

    public void delete(Long id) {
        rolFacade.delete(id);
    }
}
