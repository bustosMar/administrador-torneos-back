package com.sistema.torneos.app.domain.repository;

import com.sistema.torneos.app.domain.entity.Partido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartidoRepository extends JpaRepository<Partido, Long> {

    List<Partido> findByJornada_Torneo_IdAndGrupo_Id(
            Long id,
            Long idGrupo
    );

    List<Partido> findByJornadaId(Long id);


    @Query(value = """
            SELECT p.*
            FROM partidos p
            JOIN jornadas j
                ON j.id = p.id_jornada
            JOIN equipos_en_torneo e
                ON e.id = p.id_equipo_en_torneo_local
            WHERE j.id_torneo = :idTorneo
              AND j.estado = 'JUGADA'
              AND e.id_categoria_torneo = :idCategoria
              AND j.id = (
                  SELECT j2.id
                  FROM jornadas j2
                  WHERE j2.id_torneo = :idTorneo
                    AND j2.estado = 'JUGADA'
                    AND (
                        (j2.id_grupo IS NULL AND j.id_grupo IS NULL)
                        OR j2.id_grupo = j.id_grupo
                    )
                    AND EXISTS (
                        SELECT 1
                        FROM partidos px
                        JOIN equipos_en_torneo ex
                            ON ex.id = px.id_equipo_en_torneo_local
                        WHERE px.id_jornada = j2.id
                          AND ex.id_categoria_torneo = :idCategoria
                    )
                  ORDER BY j2.numero_jornada DESC, j2.id DESC
                  LIMIT 1
              )
            ORDER BY
                j.id_grupo ASC NULLS LAST,
                j.numero_jornada ASC,
                p.fecha ASC,
                p.hora ASC,
                p.id ASC
            """,
            nativeQuery = true)
    List<Partido> findPartidosByUltimaJornadaJugadaAndCategoria(
            @Param("idTorneo") Long idTorneo,
            @Param("idCategoria") Long idCategoria
    );


    List<Partido> findByJornada_Torneo_IdAndEquipoLocal_CategoriaTorneo_IdAndGrupo_Id(
            Long idTorneo,
            Long idCategoria,
            Long idGrupo
    );


    // =========================================================
    // PARTIDOS JUGADOS PARA TABLA DE POSICIONES
    // =========================================================

    @Query("""
            SELECT DISTINCT p
            FROM Partido p

            JOIN FETCH p.equipoLocal local
            JOIN FETCH local.equipo
            LEFT JOIN FETCH local.grupo
            JOIN FETCH local.torneo
            JOIN FETCH local.categoriaTorneo

            JOIN FETCH p.equipoVisitante visitante
            JOIN FETCH visitante.equipo
            LEFT JOIN FETCH visitante.grupo
            JOIN FETCH visitante.torneo
            JOIN FETCH visitante.categoriaTorneo

            WHERE p.jugado = true

              AND local.torneo.id = :torneoId
              AND local.categoriaTorneo.id = :categoriaId

              AND visitante.torneo.id = :torneoId
              AND visitante.categoriaTorneo.id = :categoriaId

            ORDER BY local.id ASC
            """)
    List<Partido> findPartidosJugadosPorTorneoYCategoria(
            @Param("torneoId") Long torneoId,
            @Param("categoriaId") Long categoriaId
    );
}
