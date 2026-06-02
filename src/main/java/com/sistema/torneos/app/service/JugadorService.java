package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.facade.JugadorFacade;

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

    public List<Jugador> findAll() {
        return jugadorFacade.findAll();
    }

    public Jugador findById(Long id) {
        return jugadorFacade.findById(id);
    }

    public Jugador create(Jugador jugador) {
        return jugadorFacade.create(jugador);
    }

    public Jugador update(Long id, Jugador jugador) {
        return jugadorFacade.update(id, jugador);
    }

    public void delete(Long id) {
        jugadorFacade.delete(id);
    }
}
