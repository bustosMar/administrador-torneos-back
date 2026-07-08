package com.sistema.torneos.app.service;

import com.sistema.torneos.app.facade.PartidoFacade;
import com.sistema.torneos.app.web.model.PartidoModel;

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

    public List<PartidoModel> findAll() {
        return partidoFacade.findAll();
    }

    public PartidoModel findById(Long id) {
        return partidoFacade.findById(id);
    }

    public PartidoModel create(PartidoModel partido) {
        return partidoFacade.create(partido);
    }

    public PartidoModel update(Long id, PartidoModel partido) {
        return partidoFacade.update(id, partido);
    }

    public void delete(Long id) {
        partidoFacade.delete(id);
    }

	public void createPartido(List<PartidoModel> partido) {
		partidoFacade.createPartido(partido);
		
	}
	
}
