package com.sistema.torneos.app.service;


import com.sistema.torneos.app.facade.JugadorFacade;
import com.sistema.torneos.app.web.model.JugadorModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JugadorService {

    private final JugadorFacade jugadorFacade;

    @Autowired
    public JugadorService(JugadorFacade jugadorFacade) {
        this.jugadorFacade = jugadorFacade;
    }

    public List<JugadorModel> findAll() {
        return jugadorFacade.findAll();
    }

    public JugadorModel findById(Long id) {
        return jugadorFacade.findById(id);
    }

    public JugadorModel create(JugadorModel jugador) {
        return jugadorFacade.create(jugador);
    }

    public JugadorModel update(Long id, JugadorModel jugador) {
        return jugadorFacade.update(id, jugador);
    }

    public void delete(Long id) {
        jugadorFacade.delete(id);
    }
}
