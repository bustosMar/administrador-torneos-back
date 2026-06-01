package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoEnTorneoRepository extends JpaRepository<EquipoEnTorneo, Long> {
}
