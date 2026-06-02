package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.facade.PartidoFacade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartidoService {

    private final PartidoFacade partidoFacade;

    @Autowired
    public PartidoService(PartidoFacade partidoFacade) {
        this.partidoFacade = partidoFacade;
    }

    public List<Partido> findAll() {
        return partidoFacade.findAll();
    }

    public Partido findById(Long id) {
        return partidoFacade.findById(id);
    }

    public Partido create(Partido partido) {
        return partidoFacade.create(partido);
    }

    public Partido update(Long id, Partido partido) {
        return partidoFacade.update(id, partido);
    }

    public void delete(Long id) {
        partidoFacade.delete(id);
    }
}
