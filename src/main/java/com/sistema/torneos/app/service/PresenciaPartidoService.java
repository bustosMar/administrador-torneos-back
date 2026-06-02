package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.PresenciaPartido;
import com.sistema.torneos.app.facade.PresenciaPartidoFacade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PresenciaPartidoService {

    private final PresenciaPartidoFacade presenciaPartidoFacade;

    @Autowired
    public PresenciaPartidoService(PresenciaPartidoFacade presenciaPartidoFacade) {
        this.presenciaPartidoFacade = presenciaPartidoFacade;
    }

    public List<PresenciaPartido> findAll() {
        return presenciaPartidoFacade.findAll();
    }

    public PresenciaPartido findById(Long id) {
        return presenciaPartidoFacade.findById(id);
    }

    public PresenciaPartido create(PresenciaPartido presenciaPartido) {
        return presenciaPartidoFacade.create(presenciaPartido);
    }

    public PresenciaPartido update(Long id, PresenciaPartido presenciaPartido) {
        return presenciaPartidoFacade.update(id, presenciaPartido);
    }

    public void delete(Long id) {
        presenciaPartidoFacade.delete(id);
    }
}
