package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Equipo;
import com.sistema.torneos.app.web.model.EquipoModel;
import com.sistema.torneos.app.facade.EquipoFacade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipoService {

    private final EquipoFacade equipoFacade;

    @Autowired
    public EquipoService(EquipoFacade equipoFacade) {
        this.equipoFacade = equipoFacade;
    }

    public List<EquipoModel> findAll() {
        return equipoFacade.findAll();
    }

    public EquipoModel findById(Long id) {
        return equipoFacade.findById(id);
    }

    public EquipoModel create(EquipoModel equipo) {
        return equipoFacade.create(equipo);
    }

    public EquipoModel update(Long id, EquipoModel equipo) {
        return equipoFacade.update(id, equipo);
    }

    public void delete(Long id) {
        equipoFacade.delete(id);
    }
}
