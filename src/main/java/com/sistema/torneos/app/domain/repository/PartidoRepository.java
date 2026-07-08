package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Partido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
	
	List<Partido> findByJornada_Torneo_IdAndGrupo_Id(Long id, Long idGrupo);	

    List<Partido> findByJornadaId(Long id);
    
    List<Partido> findByJornada_Torneo_IdAndEquipoLocal_CategoriaTorneo_IdAndGrupo_Id(
            Long idTorneo,
            Long idCategoria,
            Long idGrupo);
}
