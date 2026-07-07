package com.sistema.torneos.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.torneos.app.facade.JornadaFacade;
import com.sistema.torneos.app.web.model.response.JornadaResponse;

@Service
public class JornadaService {
   
    @Autowired
    private JornadaFacade jornadaFacade;

    public List<JornadaResponse> generarCalendario(Long idTorneo, Long idCategoria) {
        return jornadaFacade.crearJornadas(idTorneo, idCategoria);
    }

    public JornadaResponse generarSiguienteJornada(Long idTorneo) {
        return jornadaFacade.activarSiguienteJornada(idTorneo);
    }

    public JornadaResponse obtenerJornadaActual(Long idTorneo) {
        return jornadaFacade.obtenerJornadaActual(idTorneo);
    }
}

