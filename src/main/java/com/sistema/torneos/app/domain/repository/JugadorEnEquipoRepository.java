package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.JugadorEnEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JugadorEnEquipoRepository extends JpaRepository<JugadorEnEquipo, Long> {

	@Query("SELECT j FROM JugadorEnEquipo j WHERE j.torneo.activo = true")
	List<JugadorEnEquipo> findAllByTorneoActivo();
	
	 JugadorEnEquipo findByJugador_IdAndTorneo_IdAndActivo(
	            Long idJugador,
	            Long idTorneo,
	            boolean activo
	    );
}