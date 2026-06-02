package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Gol;
import com.sistema.torneos.app.facade.GolFacade;

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

    public List<Gol> findAll() {
        return golFacade.findAll();
    }

    public Gol findById(Long id) {
        return golFacade.findById(id);
    }

    public Gol create(Gol gol) {
        return golFacade.create(gol);
    }

    public Gol update(Long id, Gol gol) {
        return golFacade.update(id, gol);
    }

    public void delete(Long id) {
        golFacade.delete(id);
    }
}
