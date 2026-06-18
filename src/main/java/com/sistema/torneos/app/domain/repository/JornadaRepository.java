package com.sistema.torneos.app.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sistema.torneos.app.domain.entity.Jornada;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {

    Jornada findFirstByTorneoIdAndEstado(Long id, String estado);

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
