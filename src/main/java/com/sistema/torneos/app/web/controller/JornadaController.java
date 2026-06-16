package com.sistema.torneos.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.torneos.app.service.JornadaService;
import com.sistema.torneos.app.web.model.response.JornadaReponse;



@RestController
@RequestMapping("/api/jornadas")
public class JornadaController {

    @Autowired
    private JornadaService service;


    public JornadaController(JornadaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public List<JornadaReponse> generarJornada(@PathVariable("id") Long id) {
        return service.generarJornada(id);
    }
}
