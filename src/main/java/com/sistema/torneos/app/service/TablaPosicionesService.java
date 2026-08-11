package com.sistema.torneos.app.service;

import com.sistema.torneos.app.web.model.response.TablasTorneoResponse;
import com.sistema.torneos.app.web.model.response.TablasPosicionResponse;
import com.sistema.torneos.app.facade.TablaPosicionesFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TablaPosicionesService {

    private final TablaPosicionesFacade tablaPosicionesFacade;

    public TablasTorneoResponse calcularTablas(
            Long torneoId,
            Long categoriaId
    ) {
        return tablaPosicionesFacade.calcularTablas(
                torneoId,
                categoriaId
        );
    }
}