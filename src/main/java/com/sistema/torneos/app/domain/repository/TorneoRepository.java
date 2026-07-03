package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
	
	Torneo findByNombre(String nombre);

	List<Torneo> findAllByActivoTrue();

	Torneo findByNombreAndActivoTrue(String nombre);
}
