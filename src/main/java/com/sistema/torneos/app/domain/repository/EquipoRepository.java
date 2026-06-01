package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
}
