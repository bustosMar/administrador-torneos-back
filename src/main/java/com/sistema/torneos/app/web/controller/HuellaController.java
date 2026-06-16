package com.sistema.torneos.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sistema.torneos.app.web.model.response.HuellaResponse;

import com.sistema.torneos.app.service.HuellaService;

@RestController
@RequestMapping("/api/huellas")
public class HuellaController {

	@Autowired
    private final HuellaService huellaService;
    
    public HuellaController(HuellaService huellaService) {
        this.huellaService = huellaService;
    }

    @PostMapping("/capturar")
    public ResponseEntity<HuellaResponse> capturar() {

        HuellaResponse response =
                huellaService.capturarHuella();

        return ResponseEntity.ok(response);
    }

}
