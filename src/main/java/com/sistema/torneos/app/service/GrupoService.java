package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Grupo;
import com.sistema.torneos.app.facade.GrupoFacade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrupoService {

    private final GrupoFacade grupoFacade;

    @Autowired
    public GrupoService(GrupoFacade grupoFacade) {
        this.grupoFacade = grupoFacade;
    }

    public List<Grupo> findAll() {
        return grupoFacade.findAll();
    }

    public Grupo findById(Long id) {
        return grupoFacade.findById(id);
    }

    public Grupo create(Grupo grupo) {
        return grupoFacade.create(grupo);
    }

    public Grupo update(Long id, Grupo grupo) {
        return grupoFacade.update(id, grupo);
    }

    public void delete(Long id) {
        grupoFacade.delete(id);
    }
}
