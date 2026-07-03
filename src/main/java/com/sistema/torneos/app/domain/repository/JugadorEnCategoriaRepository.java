package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.JugadorEnCategoria;
import com.sistema.torneos.app.domain.entity.CategoriaTorneo;
import com.sistema.torneos.app.domain.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorEnCategoriaRepository extends JpaRepository<JugadorEnCategoria, Long> {
    
    List<JugadorEnCategoria> findByJugadorAndActivoTrue(Jugador jugador);
    
    Optional<JugadorEnCategoria> findByJugadorIdAndCategoriaTorneoId(Long jugadorId, Long categoriaTorneoId);
    
    List<JugadorEnCategoria> findByCategoriaTorneoAndActivoTrue(CategoriaTorneo categoriaTorneo);
    
    boolean existsByJugadorIdAndCategoriaTorneoId(Long jugadorId, Long categoriaTorneoId);
    
    List<JugadorEnCategoria> findByCategoriaTorneo(CategoriaTorneo categoriaTorneo);
    
    // Contar inscripciones activas de un jugador en un torneo
    long countByJugadorIdAndCategoriaTorneoTorneoIdAndActivoTrue(Long jugadorId, Long torneoId);
    
    // Obtener inscripciones activas de un jugador en categorías de un torneo
    List<JugadorEnCategoria> findByJugadorIdAndCategoriaTorneoTorneoIdAndActivoTrue(Long jugadorId, Long torneoId);
}
