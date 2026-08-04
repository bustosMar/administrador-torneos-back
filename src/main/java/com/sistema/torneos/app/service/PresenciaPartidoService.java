package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.PresenciaPartido;
import com.sistema.torneos.app.facade.PresenciaPartidoFacade;
import com.sistema.torneos.app.web.model.request.GuardarPresenciasPartidoRequest;
import com.sistema.torneos.app.web.model.request.RegistrarPresenciaHuellaRequest;
import com.sistema.torneos.app.web.model.response.GuardarPresenciasPartidoResponse;
import com.sistema.torneos.app.web.model.response.PresenciaPartidoDetalleResponse;
import com.sistema.torneos.app.web.model.response.RegistroPresenciaHuellaResponse;

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

    public PresenciaPartidoDetalleResponse getDetalleByPartido(Long idPartido) {
	return presenciaPartidoFacade.getDetalleByPartido(idPartido);
    }

    public PresenciaPartido create(PresenciaPartido presenciaPartido) {
        return presenciaPartidoFacade.create(presenciaPartido);
    }

    public PresenciaPartido update(Long id, PresenciaPartido presenciaPartido) {
        return presenciaPartidoFacade.update(id, presenciaPartido);
    }

    public RegistroPresenciaHuellaResponse registrarPorHuella(Long idPartido, RegistrarPresenciaHuellaRequest request) {
	return presenciaPartidoFacade.registrarPorHuella(idPartido, request);
    }

    public GuardarPresenciasPartidoResponse guardarPresencias(Long idPartido, GuardarPresenciasPartidoRequest request) {
	return presenciaPartidoFacade.guardarPresencias(idPartido, request);
    }

    public void delete(Long id) {
        presenciaPartidoFacade.delete(id);
    }
}
