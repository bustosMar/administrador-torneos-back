package com.sistema.torneos.app.service;

import com.sistema.torneos.app.facade.TorneoFacade;
import com.sistema.torneos.app.web.model.TorneoModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TorneoService {

    private final TorneoFacade torneoFacade;

    @Autowired
    public TorneoService(TorneoFacade torneoFacade) {
        this.torneoFacade = torneoFacade;
    }

    public List<TorneoModel> findAll() {
        return torneoFacade.findAll();
    }

    public TorneoModel findById(Long id) {
        return torneoFacade.findById(id);
    }

    public TorneoModel create(TorneoModel torneo) {
        return torneoFacade.create(torneo);
    }

    public TorneoModel update(Long id, TorneoModel torneo) {
        return torneoFacade.update(id, torneo);
    }

    public void delete(Long id) {
        torneoFacade.delete(id);
    }
}
