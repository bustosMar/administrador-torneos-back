package com.sistema.torneos.app.service;

import com.sistema.torneos.app.facade.JugadorEnEquipoFacade;
import com.sistema.torneos.app.web.model.JugadorEnEquipoModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JugadorEnEquipoService {

    private final JugadorEnEquipoFacade jugadorEnEquipoFacade;

    @Autowired
    public JugadorEnEquipoService(JugadorEnEquipoFacade jugadorEnEquipoFacade) {
        this.jugadorEnEquipoFacade = jugadorEnEquipoFacade;
    }

    public List<JugadorEnEquipoModel> findAll() {
        return jugadorEnEquipoFacade.findAll();
    }

    public JugadorEnEquipoModel findById(Long id) {
        return jugadorEnEquipoFacade.findById(id);
    }

    public JugadorEnEquipoModel create(JugadorEnEquipoModel jugadorEnEquipo) {
        return jugadorEnEquipoFacade.create(jugadorEnEquipo);
    }

    public JugadorEnEquipoModel update(Long id, JugadorEnEquipoModel jugadorEnEquipo) {
        return jugadorEnEquipoFacade.update(id, jugadorEnEquipo);
    }

    public void delete(Long id) {
        jugadorEnEquipoFacade.delete(id);
    }
}