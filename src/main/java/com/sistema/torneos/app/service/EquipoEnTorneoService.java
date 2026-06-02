package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.facade.EquipoEnTorneoFacade;

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

    public List<EquipoEnTorneo> findAll() {
        return equipoEnTorneoFacade.findAll();
    }

    public EquipoEnTorneo findById(Long id) {
        return equipoEnTorneoFacade.findById(id);
    }

    public EquipoEnTorneo create(EquipoEnTorneo equipoEnTorneo) {
        return equipoEnTorneoFacade.create(equipoEnTorneo);
    }

    public EquipoEnTorneo update(Long id, EquipoEnTorneo equipoEnTorneo) {
        return equipoEnTorneoFacade.update(id, equipoEnTorneo);
    }

    public void delete(Long id) {
        equipoEnTorneoFacade.delete(id);
    }
}
