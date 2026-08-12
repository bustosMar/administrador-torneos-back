package com.sistema.torneos.app.service;

import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.facade.SancionFacade;
import com.sistema.torneos.app.web.model.SancionModel;
import com.sistema.torneos.app.web.model.response.JugadoresSancionesSuspensionesResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SancionService {

    private final SancionFacade sancionFacade;

    @Autowired
    public SancionService(SancionFacade sancionFacade) {
        this.sancionFacade = sancionFacade;
    }

    // =========================================================
    // SANCIONES
    // =========================================================

    public List<SancionModel> findAll() {
        return sancionFacade.findAll();
    }

    public SancionModel findById(Long id) {
        return sancionFacade.findById(id);
    }

    public SancionModel create(SancionModel sancion) {
        return sancionFacade.create(sancion);
    }

    public SancionModel update(
            Long id,
            SancionModel sancion
    ) {
        return sancionFacade.update(id, sancion);
    }

    public void delete(Long id) {
        sancionFacade.delete(id);
    }

    // =========================================================
    // BUSCAR JUGADORES
    //
    // Este método recibe STRING porque aquí buscamos por:
    //
    // Mar
    // Bus
    // Mar Bus
    //
    // =========================================================

    public List<Jugador>
    buscarJugadores(String nombre) {

        return sancionFacade.buscarJugadores(nombre);
    }

    // =========================================================
    // BUSCAR SANCIONES Y SUSPENSIONES POR JUGADOR
    //
    // IMPORTANTE:
    //
    // Este método recibe Long porque ya seleccionamos
    // un jugador específico.
    //
    // =========================================================

    public JugadoresSancionesSuspensionesResponse
    buscarSancionesSuspensionesPorJugador(
            Long jugadorId
    ) {

        return sancionFacade.buscarSancionesSuspensionesPorJugador(
                jugadorId
        );
    }
}