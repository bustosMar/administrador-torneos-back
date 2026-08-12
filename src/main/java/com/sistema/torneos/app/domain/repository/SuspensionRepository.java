package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Suspension;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SuspensionRepository extends JpaRepository<Suspension, Long> {
	
	List<Suspension> findByJugadorIdOrderByFechaInicioDesc(
            Long jugadorId
    );
	
	 List<Suspension> findByJugadorId(Long jugadorId);
}
