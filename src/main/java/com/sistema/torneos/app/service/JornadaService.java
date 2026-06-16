package com.sistema.torneos.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.torneos.app.facade.JornadaFacade;
import com.sistema.torneos.app.web.model.response.JornadaReponse;

@Service
public class JornadaService {

    private JornadaFacade jornadaFacade;
    
    @Autowired
    public JornadaService(JornadaFacade jornadaFacade) {
        this.jornadaFacade = jornadaFacade;
    }

    public List<JornadaReponse> generarJornada(Long idTorneo) {
        return jornadaFacade.generarJornada(idTorneo);
    }
}

