package com.sistema.torneos.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.torneos.app.facade.JornadaFacade;
import com.sistema.torneos.app.web.model.JugadorModel;
import com.sistema.torneos.app.web.model.response.JornadaResponse;

@Service
public class JornadaService {
   
    @Autowired
    private JornadaFacade jornadaFacade;

    public List<JornadaResponse> generarCalendario(Long idTorneo, Long idCategoria) {
        return jornadaFacade.crearJornadas(idTorneo, idCategoria);
    }

    public JornadaResponse obtenerJornadaActual(Long idTorneo, Long idCategoria) {
        return jornadaFacade.obtenerJornadaActual(idTorneo, idCategoria);
    }
    
    public JornadaResponse previsualizarSiguienteJornada(
            Long idTorneo,
            Long idCategoria) {

        return jornadaFacade.previsualizarSiguienteJornada(
                idTorneo,
                idCategoria);
    }

	public void update(Long id) {
		return jornadaFacade.update(id);
	}
    
}

