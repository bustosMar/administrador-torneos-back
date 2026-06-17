package com.sistema.torneos.app.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.torneos.app.domain.entity.Jornada;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {

    Jornada findFirstByTorneoIdAndEstado(Long torneoId, String estado);

    List<Jornada> findByTorneoId(Long torneoId);
    
    Jornada findTopByGrupoIdOrderByNumeroJornadaDesc(Long grupoId);
    
    Boolean existsByGrupoIdAndNumeroJornada(Long id, int numero);
}
