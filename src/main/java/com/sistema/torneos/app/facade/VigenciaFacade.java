package com.sistema.torneos.app.facade;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.torneos.app.domain.entity.Vigencia;
import com.sistema.torneos.app.domain.repository.VigenciaRepository;


@Component
@Transactional(readOnly = true)
public class VigenciaFacade {
	
	private final VigenciaRepository vigenciaRepository;
	
	@Autowired
	public VigenciaFacade (VigenciaRepository vigenciaRepository) {
		this.vigenciaRepository = vigenciaRepository;
	}
	

    public Vigencia obtenerVigenciaActual(String clave) {
        return vigenciaRepository.findVigenciaActiva(clave,LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No hay vigencia activa"));
    }

}
