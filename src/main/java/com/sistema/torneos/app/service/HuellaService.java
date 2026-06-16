package com.sistema.torneos.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.torneos.app.facade.HuellaFacade;
import com.sistema.torneos.app.web.model.response.HuellaResponse;


@Service
public class HuellaService {

	@Autowired
    private final HuellaFacade huellaFacade;
    
    public HuellaService(HuellaFacade huellaFacade) {
        this.huellaFacade = huellaFacade;
    }

    public HuellaResponse capturarHuella() {

        String template =
                huellaFacade.capturarTemplate();

        return HuellaResponse.builder()
                .template(template)
                .capturada(true)
                .build();
    }

}
