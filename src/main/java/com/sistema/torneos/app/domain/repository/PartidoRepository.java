package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Partido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
	
	List<Partido> findByTorneoIdAndGrupoId(
            Long idTorneo,
            Long idGrupo);	
}
