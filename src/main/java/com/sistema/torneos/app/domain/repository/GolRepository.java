package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Gol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GolRepository extends JpaRepository<Gol, Long> {

    @Query("""
            SELECT g
            FROM Gol g
            JOIN FETCH g.partido partido
            JOIN FETCH g.equipoTorneo equipo
            WHERE partido.id IN :partidoIds
            ORDER BY partido.id ASC, g.id ASC
            """)
    List<Gol> findGolesByPartidoIds(
            @Param("partidoIds") List<Long> partidoIds
    );
    
    @Query("""
            SELECT
                g.jugador.id,
                CONCAT(
                    g.jugador.nombre,
                    ' ',
                    g.jugador.apellido
                ),
                g.equipoTorneo.id,
                g.equipoTorneo.equipo.nombre,
                COUNT(g.id)

            FROM Gol g

            JOIN g.partido p

            JOIN g.jugador jugador

            JOIN g.equipoTorneo equipo

            JOIN equipo.equipo club

            JOIN equipo.torneo torneo

            JOIN equipo.categoriaTorneo categoria

            WHERE p.jugado = true

              AND torneo.id = :torneoId

              AND categoria.id = :categoriaId

            GROUP BY
                g.jugador.id,
                g.jugador.nombre,
                g.jugador.apellido,
                g.equipoTorneo.id,
                g.equipoTorneo.equipo.nombre

            ORDER BY
                COUNT(g.id) DESC,
                g.jugador.apellido ASC,
                g.jugador.nombre ASC
            """)
        List<Object[]> findTablaGoleo(
                @Param("torneoId") Long torneoId,
                @Param("categoriaId") Long categoriaId
        );
    
        @Query("""
        	    SELECT g
        	    FROM Gol g
        	    JOIN FETCH g.jugador jugador
        	    JOIN FETCH g.equipoTorneo equipoTorneo
        	    JOIN FETCH equipoTorneo.equipo equipo
        	    JOIN FETCH equipoTorneo.torneo torneo
        	    JOIN FETCH equipoTorneo.categoriaTorneo categoria
        	    WHERE torneo.id = :torneoId
        	      AND categoria.id = :categoriaId
        	""")
        	List<Gol> findGolesJugadoresPorTorneoYCategoria(
        	        @Param("torneoId") Long torneoId,
        	        @Param("categoriaId") Long categoriaId
        	);
    
    
}
