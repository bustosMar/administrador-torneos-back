package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.PresenciaPartido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresenciaPartidoRepository extends JpaRepository<PresenciaPartido, Long> {
}
