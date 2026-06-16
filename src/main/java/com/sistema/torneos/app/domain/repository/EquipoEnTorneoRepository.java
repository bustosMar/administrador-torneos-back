package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipoEnTorneoRepository extends JpaRepository<EquipoEnTorneo, Long> {
	
	EquipoEnTorneo findByEquipo_IdAndTorneo_Id(Long idEquipo, Long idTorneo);
	
	EquipoEnTorneo findByEquipo_IdAndTorneo_IdAndGrupo_Id(Long equipoId,Long torneoId,Long grupoId);

	@Query("SELECT e FROM EquipoEnTorneo e WHERE e.torneo.id = :torneoId")
	List<EquipoEnTorneo> getByTorneo(@Param("torneoId") Long torneoId);
	
}
