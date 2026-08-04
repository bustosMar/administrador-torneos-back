package com.sistema.torneos.app.service;

import com.sistema.torneos.app.facade.GolFacade;
import com.sistema.torneos.app.web.model.GolModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GolService {

    private final GolFacade golFacade;

    @Autowired
    public GolService(GolFacade golFacade) {
        this.golFacade = golFacade;
    }

    public List<GolModel> findAll() {
        return golFacade.findAll();
    }

    public GolModel findById(Long id) {
        return golFacade.findById(id);
    }

    public GolModel create(GolModel gol) {
        return golFacade.create(gol);
    }

    public GolModel update(Long id, GolModel gol) {
        return golFacade.update(id, gol);
    }

    public void delete(Long id) {
        golFacade.delete(id);
    }
}
