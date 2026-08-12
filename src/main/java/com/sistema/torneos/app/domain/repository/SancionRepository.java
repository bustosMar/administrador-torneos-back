package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Sancion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SancionRepository extends JpaRepository<Sancion, Long> {

    long countByPartidoIdAndJugadorIdAndTipoIgnoreCase(Long partidoId, Long jugadorId, String tipo);
    
    List<Sancion> findByJugadorIdOrderByPartidoIdAscMinutoAsc(
            Long jugadorId
    );
    
    List<Sancion> findByJugadorId(Long jugadorId);
    
    
}
