package com.sistema.torneos.app.service;

import org.springframework.stereotype.Service;

import com.sistema.torneos.app.domain.entity.Vigencia;
import com.sistema.torneos.app.facade.VigenciaFacade;

import java.time.LocalDate;

@Service
public class VigenciaService {

    private final VigenciaFacade vigenciaFacade;

    public VigenciaService(VigenciaFacade vigenciaFacade) {
        this.vigenciaFacade = vigenciaFacade;
    }

    public Vigencia obtenerVigenciaActual(String clave) {
        return vigenciaFacade.obtenerVigenciaActual(clave);
    }
}
