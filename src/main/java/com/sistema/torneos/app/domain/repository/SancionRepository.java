package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Sancion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SancionRepository extends JpaRepository<Sancion, Long> {

    long countByPartidoIdAndJugadorIdAndTipoIgnoreCase(Long partidoId, Long jugadorId, String tipo);
}
