package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.CategoriaTorneo;
import com.sistema.torneos.app.domain.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaTorneoRepository extends JpaRepository<CategoriaTorneo, Long> {
    
    List<CategoriaTorneo> findByTorneoAndActivaTrue(Torneo torneo);
    
    List<CategoriaTorneo> findByTorneo(Torneo torneo);
    
    Optional<CategoriaTorneo> findByTorneoIdAndCategoriaId(Long torneoId, Long categoriaId);
    
    Optional<CategoriaTorneo> findByTorneoIdAndCategoriaNombre(Long torneoId, String categoriaNombre);
}
