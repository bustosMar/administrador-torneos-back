package com.sistema.torneos.app.service;

import com.sistema.torneos.app.web.model.ArbitroModel;
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

    public List<ArbitroModel> findAll() {
        return arbitroFacade.findAll();
    }

    public ArbitroModel findById(Long id) {
        return arbitroFacade.findById(id);
    }

    public ArbitroModel create(ArbitroModel arbitro) {
        return arbitroFacade.create(arbitro);
    }

    public ArbitroModel update(Long id, ArbitroModel arbitro) {
        return arbitroFacade.update(id, arbitro);
    }

    public void delete(Long id) {
        arbitroFacade.delete(id);
    }
}
