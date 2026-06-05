package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Grupo;
import com.sistema.torneos.app.facade.GrupoFacade;
import com.sistema.torneos.app.web.model.GrupoModel;

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

    public List<GrupoModel> findAll() {
        return grupoFacade.findAll();
    }

    public GrupoModel findById(Long id) {
        return grupoFacade.findById(id);
    }

    public GrupoModel create(GrupoModel grupo) {
        return grupoFacade.create(grupo);
    }

    public GrupoModel update(Long id, GrupoModel grupo) {
        return grupoFacade.update(id, grupo);
    }

    public void delete(Long id) {
        grupoFacade.delete(id);
    }
}
