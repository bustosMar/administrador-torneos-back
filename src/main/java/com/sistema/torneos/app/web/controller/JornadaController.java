package com.sistema.torneos.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.torneos.app.service.JornadaService;
import com.sistema.torneos.app.web.model.response.JornadaReponse;
import com.sistema.torneos.app.web.model.response.JornadaResponse;



@RestController
@RequestMapping("/api/jornadas")
public class JornadaController {

	 @Autowired
	    private JornadaService jornadaService;

	    /**
	     * 1. Genera calendario (solo jornadas)
	     */
		 @PostMapping("/{idTorneo}/{idCategoria}/calendario")
		 public List<JornadaResponse> generarCalendario(
		         @PathVariable Long idTorneo,
		         @PathVariable Long idCategoria) {
	
		     return jornadaService.generarCalendario(
		             idTorneo,
		             idCategoria);
		 }

	    /**
	     * 2. Activa siguiente jornada (crea partidos)
	     */
	    @PostMapping("/{idTorneo}/jornadas/siguiente")
	    public JornadaResponse generarSiguiente(@PathVariable Long idTorneo) {
	        return jornadaService.generarSiguienteJornada(idTorneo);
	    }

	    /**
	     * 3. Ver jornada actual
	     */
	    @GetMapping("/{idTorneo}/jornadas/actual")
	    public JornadaResponse actual(@PathVariable Long idTorneo) {
	        return jornadaService.obtenerJornadaActual(idTorneo);
	    }
	    
	    @GetMapping("/{idTorneo}/{idCategoria}/previsualizar-partidos")
	    public JornadaResponse previsualizarPartidos(
	            @PathVariable Long idTorneo,
	            @PathVariable Long idCategoria) {

	        return jornadaService.previsualizarSiguienteJornada(
	                idTorneo,
	                idCategoria);
	    }
}
