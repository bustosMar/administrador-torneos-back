package com.sistema.torneos.app.service;

import com.sistema.torneos.app.facade.TablaGoleoFacade;
import com.sistema.torneos.app.web.model.response.TablaGoleoResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TablaGoleoService {

    private final TablaGoleoFacade tablaGoleoFacade;


    @Transactional(readOnly = true)
    public TablaGoleoResponse calcularTablaGoleo(
            Long torneoId,
            Long categoriaId
    ) {

        return tablaGoleoFacade.calcularTablaGoleo(
                torneoId,
                categoriaId
        );
    }
}