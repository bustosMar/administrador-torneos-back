package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipoEnTorneoRepository extends JpaRepository<EquipoEnTorneo, Long> {
	
	List<EquipoEnTorneo> findByEquipo_IdAndTorneo_Id(Long equipoId, Long torneoId);
	
	EquipoEnTorneo findByEquipo_IdAndTorneo_IdAndGrupo_Id(Long equipoId,Long torneoId,Long grupoId);

	@Query("SELECT e FROM EquipoEnTorneo e WHERE e.torneo.id = :torneoId")
	List<EquipoEnTorneo> getByTorneo(@Param("torneoId") Long torneoId);
	
	List<EquipoEnTorneo> getByGrupoId(Long id);

	@Query("SELECT e FROM EquipoEnTorneo e WHERE e.torneo.activo = true")
	List<EquipoEnTorneo> findAllByTorneoActivo();
	
	EquipoEnTorneo findByEquipo_IdAndTorneo_IdAndCategoriaTorneo_IdAndGrupo_Id(Long equipoId, Long torneoId, Long categoriaTorneoId, Long grupoId);
	
	EquipoEnTorneo findByEquipo_IdAndTorneo_IdAndCategoriaTorneo_Id(Long equipoId, Long torneoId, Long categoriaTorneoId);
	
}
