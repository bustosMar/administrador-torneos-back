package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.CategoriaTorneo;
import com.sistema.torneos.app.domain.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaTorneoRepository extends JpaRepository<CategoriaTorneo, Long> {

	@Query("SELECT c FROM CategoriaTorneo c WHERE c.torneo.activo = true")
	List<CategoriaTorneo> findAllByTorneoActivo();
    
    List<CategoriaTorneo> findByTorneoAndActivaTrue(Torneo torneo);
    
    List<CategoriaTorneo> findByTorneo(Torneo torneo);
    
    List<CategoriaTorneo> findByTorneoIdAndCategoriaId(Long torneoId, Long categoriaId);
    
    List<CategoriaTorneo> findByTorneoIdAndCategoriaNombre(Long torneoId, String categoriaNombre);
    
    @Query("SELECT c FROM CategoriaTorneo c WHERE c.torneo.id = :torneoId")
    List<CategoriaTorneo> findByTorneoId(Long torneoId);
}
