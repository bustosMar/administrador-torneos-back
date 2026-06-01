package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
}
