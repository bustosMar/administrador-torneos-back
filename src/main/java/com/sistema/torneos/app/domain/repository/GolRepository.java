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
}
