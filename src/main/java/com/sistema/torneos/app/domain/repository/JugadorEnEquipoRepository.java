package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.JugadorEnEquipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorEnEquipoRepository extends JpaRepository<JugadorEnEquipo, Long> {
	
	 JugadorEnEquipo findByJugador_IdAndTorneo_IdAndActivo(
	            Long idJugador,
	            Long idTorneo,
	            boolean activo
	    );
}