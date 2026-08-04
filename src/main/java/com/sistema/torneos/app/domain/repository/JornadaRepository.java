package com.sistema.torneos.app.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.torneos.app.domain.entity.Jornada;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {
	
	@Query(value = """
			SELECT j.*
			FROM jornadas j
			WHERE j.id_torneo = :torneoId
			AND j.estado = :estado
			AND (
				NOT EXISTS (
					SELECT 1
					FROM partidos p0
					WHERE p0.id_jornada = j.id
				)
				OR EXISTS (
					SELECT 1
					FROM partidos p
					JOIN equipos_en_torneo e
						ON e.id = p.id_equipo_en_torneo_local
					WHERE p.id_jornada = j.id
					AND e.id_categoria_torneo = :categoriaId
				)
			)
			ORDER BY j.numero_jornada ASC, j.id ASC
			LIMIT 1
			""", nativeQuery = true)
	Jornada findPrimeraJornadaPorTorneoEstadoYCategoria(@Param("torneoId") Long id, @Param("estado") String estado, @Param("categoriaId") Long idCategoria);

    List<Jornada> findByTorneoId(Long id);
    
    Jornada findTopByGrupoIdOrderByNumeroJornadaDesc(Long id);
    
    Boolean existsByGrupoIdAndNumeroJornada(Long id, int numero);
    
    List<Jornada> findByTorneoIdAndEstado(Long id, String estatus);
    
    @Query(value = """
    	    SELECT setval(
    	        pg_get_serial_sequence('jornadas','id'),
    	        COALESCE((SELECT MAX(id) FROM jornadas),0) + 1,
    	        false
    	    )
    	    """, nativeQuery = true)
    	Long sincronizarSecuencia();

    
}
