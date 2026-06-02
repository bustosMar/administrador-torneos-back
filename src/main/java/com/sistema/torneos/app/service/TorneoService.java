package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.facade.TorneoFacade;

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

    public List<Torneo> findAll() {
        return torneoFacade.findAll();
    }

    public Torneo findById(Long id) {
        return torneoFacade.findById(id);
    }

    public Torneo create(Torneo torneo) {
        return torneoFacade.create(torneo);
    }

    public Torneo update(Long id, Torneo torneo) {
        return torneoFacade.update(id, torneo);
    }

    public void delete(Long id) {
        torneoFacade.delete(id);
    }
}
