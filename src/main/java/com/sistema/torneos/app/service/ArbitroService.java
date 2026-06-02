package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Arbitro;
import com.sistema.torneos.app.facade.ArbitroFacade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArbitroService {

    private final ArbitroFacade arbitroFacade;

    @Autowired
    public ArbitroService(ArbitroFacade arbitroFacade) {
        this.arbitroFacade = arbitroFacade;
    }

    public List<Arbitro> findAll() {
        return arbitroFacade.findAll();
    }

    public Arbitro findById(Long id) {
        return arbitroFacade.findById(id);
    }

    public Arbitro create(Arbitro arbitro) {
        return arbitroFacade.create(arbitro);
    }

    public Arbitro update(Long id, Arbitro arbitro) {
        return arbitroFacade.update(id, arbitro);
    }

    public void delete(Long id) {
        arbitroFacade.delete(id);
    }
}
