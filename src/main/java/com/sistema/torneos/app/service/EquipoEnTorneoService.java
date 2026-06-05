package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.facade.EquipoEnTorneoFacade;
import com.sistema.torneos.app.web.model.EquipoEnTorneoModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipoEnTorneoService {

    private final EquipoEnTorneoFacade equipoEnTorneoFacade;

    @Autowired
    public EquipoEnTorneoService(EquipoEnTorneoFacade equipoEnTorneoFacade) {
        this.equipoEnTorneoFacade = equipoEnTorneoFacade;
    }

    public List<EquipoEnTorneoModel> findAll() {
        return equipoEnTorneoFacade.findAll();
    }

    public EquipoEnTorneoModel findById(Long id) {
        return equipoEnTorneoFacade.findById(id);
    }

    public EquipoEnTorneoModel create(EquipoEnTorneoModel equipoEnTorneo) {
        return equipoEnTorneoFacade.create(equipoEnTorneo);
    }

    public EquipoEnTorneoModel update(Long id, EquipoEnTorneoModel equipoEnTorneo) {
        return equipoEnTorneoFacade.update(id, equipoEnTorneo);
    }

    public void delete(Long id) {
        equipoEnTorneoFacade.delete(id);
    }
}
