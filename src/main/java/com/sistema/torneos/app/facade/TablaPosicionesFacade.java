package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.entity.Gol;
import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.repository.GolRepository;
import com.sistema.torneos.app.domain.repository.PartidoRepository;
import com.sistema.torneos.app.web.model.response.TablasPosicionResponse;
import com.sistema.torneos.app.web.model.response.TablasTorneoResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TablaPosicionesFacade {

    private final PartidoRepository partidoRepository;

    private final GolRepository golRepository;


    // =========================================================
    // CALCULAR TABLAS
    // =========================================================

    public TablasTorneoResponse calcularTablas(
            Long torneoId,
            Long categoriaId
    ) {

        // =====================================================
        // OBTENER PARTIDOS JUGADOS
        // =====================================================

        List<Partido> partidos =
                partidoRepository.findPartidosJugadosPorTorneoYCategoria(
                        torneoId,
                        categoriaId
                );


        // =====================================================
        // OBTENER IDS DE LOS PARTIDOS
        // =====================================================

        List<Long> partidoIds =
                partidos.stream()
                        .map(Partido::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());


        // =====================================================
        // RECUPERAR TODOS LOS GOLES
        //
        // Se hace UNA SOLA consulta para todos los partidos.
        // =====================================================

        Map<Long, List<Gol>> golesPorPartido =
                new HashMap<>();


        if (!partidoIds.isEmpty()) {

            List<Gol> goles =
                    golRepository.findGolesByPartidoIds(
                            partidoIds
                    );


            golesPorPartido =
                    goles.stream()
                            .filter(
                                    gol ->
                                            gol != null &&
                                            gol.getPartido() != null
                            )
                            .collect(
                                    Collectors.groupingBy(
                                            gol ->
                                                    gol.getPartido().getId()
                                    )
                            );
        }


        // =====================================================
        // MAPA DE ESTADÍSTICAS
        //
        // equipoEnTorneo.id -> estadísticas
        // =====================================================

        Map<Long, EstadisticaEquipo> estadisticas =
                new HashMap<>();


        // =========================================================
        // PROCESAR PARTIDOS
        // =========================================================

        for (Partido partido : partidos) {

            if (partido == null) {
                continue;
            }


            EquipoEnTorneo local =
                    partido.getEquipoLocal();

            EquipoEnTorneo visitante =
                    partido.getEquipoVisitante();


            if (local == null || visitante == null) {
                continue;
            }


            // =====================================================
            // CREAR ESTADÍSTICA LOCAL
            // =====================================================

            EstadisticaEquipo estadisticaLocal =
                    estadisticas.computeIfAbsent(
                            local.getId(),
                            id -> crearEstadistica(local)
                    );


            // =====================================================
            // CREAR ESTADÍSTICA VISITANTE
            // =====================================================

            EstadisticaEquipo estadisticaVisitante =
                    estadisticas.computeIfAbsent(
                            visitante.getId(),
                            id -> crearEstadistica(visitante)
                    );


            // =====================================================
            // OBTENER GOLES DEL PARTIDO
            // =====================================================

            List<Gol> golesPartido =
                    golesPorPartido.getOrDefault(
                            partido.getId(),
                            List.of()
                    );


            int golesLocal = 0;

            int golesVisitante = 0;


            // =====================================================
            // CONTAR GOLES
            // =====================================================

            for (Gol gol : golesPartido) {

                if (
                        gol == null ||
                        gol.getEquipoTorneo() == null
                ) {
                    continue;
                }


                Long equipoGolId =
                        gol.getEquipoTorneo().getId();


                // -------------------------------------------------
                // GOL DEL LOCAL
                // -------------------------------------------------

                if (
                        Objects.equals(
                                equipoGolId,
                                local.getId()
                        )
                ) {

                    golesLocal++;

                }


                // -------------------------------------------------
                // GOL DEL VISITANTE
                // -------------------------------------------------

                else if (
                        Objects.equals(
                                equipoGolId,
                                visitante.getId()
                        )
                ) {

                    golesVisitante++;
                }
            }


            // =====================================================
            // PARTIDOS JUGADOS
            // =====================================================

            estadisticaLocal.partidosJugados++;

            estadisticaVisitante.partidosJugados++;


            // =====================================================
            // GOLES LOCAL
            // =====================================================

            estadisticaLocal.golesFavor +=
                    golesLocal;

            estadisticaLocal.golesContra +=
                    golesVisitante;


            // =====================================================
            // GOLES VISITANTE
            // =====================================================

            estadisticaVisitante.golesFavor +=
                    golesVisitante;

            estadisticaVisitante.golesContra +=
                    golesLocal;


            // =====================================================
            // RESULTADO
            // =====================================================

            if (golesLocal > golesVisitante) {

                // =================================================
                // GANA LOCAL
                // =================================================

                estadisticaLocal.partidosGanados++;

                estadisticaLocal.puntos += 3;

                estadisticaVisitante.partidosPerdidos++;


            } else if (golesLocal < golesVisitante) {

                // =================================================
                // GANA VISITANTE
                // =================================================

                estadisticaVisitante.partidosGanados++;

                estadisticaVisitante.puntos += 3;

                estadisticaLocal.partidosPerdidos++;


            } else {

                // =================================================
                // EMPATE
                // =================================================

                estadisticaLocal.partidosEmpatados++;

                estadisticaVisitante.partidosEmpatados++;

                estadisticaLocal.puntos++;

                estadisticaVisitante.puntos++;
            }
        }


        // =========================================================
        // TABLA GENERAL
        // =========================================================

        List<TablasPosicionResponse> tablaGeneral =
                estadisticas.values()
                        .stream()
                        .map(this::convertirDTO)
                        .sorted(comparadorTabla())
                        .collect(Collectors.toList());


        asignarPosiciones(tablaGeneral);


        // =========================================================
        // TABLAS POR GRUPO
        // =========================================================

        Map<Long, List<TablasPosicionResponse>> tablasPorGrupo =
                estadisticas.values()
                        .stream()
                        .map(this::convertirDTO)
                        .filter(
                                e ->
                                        e.getGrupoId() != null
                        )
                        .collect(
                                Collectors.groupingBy(
                                        TablasPosicionResponse::getGrupoId,
                                        LinkedHashMap::new,
                                        Collectors.toList()
                                )
                        );


        // =========================================================
        // ORDENAR CADA GRUPO
        // =========================================================

        tablasPorGrupo.values().forEach(tabla -> {

            tabla.sort(comparadorTabla());

            asignarPosiciones(tabla);

        });


        // =========================================================
        // RESPUESTA FINAL
        // =========================================================

        return TablasTorneoResponse.builder()
                .general(tablaGeneral)
                .porGrupo(tablasPorGrupo)
                .build();
    }


    // =========================================================
    // CREAR ESTADÍSTICA
    // =========================================================

    private EstadisticaEquipo crearEstadistica(
            EquipoEnTorneo equipo
    ) {

        EstadisticaEquipo estadistica =
                new EstadisticaEquipo();


        // =====================================================
        // EQUIPO
        // =====================================================

        estadistica.equipoId =
                equipo.getId();


        if (equipo.getEquipo() != null) {

            estadistica.nombreEquipo =
                    equipo.getEquipo().getNombre();

        } else {

            estadistica.nombreEquipo =
                    "Equipo sin nombre";
        }


        // =====================================================
        // GRUPO
        // =====================================================

        if (equipo.getGrupo() != null) {

            estadistica.grupoId =
                    equipo.getGrupo().getId();

            estadistica.nombreGrupo =
                    equipo.getGrupo().getNombre();
        }


        return estadistica;
    }


    // =========================================================
    // CONVERTIR A RESPONSE
    // =========================================================

    private TablasPosicionResponse convertirDTO(
            EstadisticaEquipo e
    ) {

        int diferenciaGoles =
                e.golesFavor - e.golesContra;


        return TablasPosicionResponse.builder()

                .equipoId(
                        e.equipoId
                )

                .equipo(
                        e.nombreEquipo
                )

                .grupoId(
                        e.grupoId
                )

                .grupo(
                        e.nombreGrupo
                )

                .partidosJugados(
                        e.partidosJugados
                )

                .partidosGanados(
                        e.partidosGanados
                )

                .partidosEmpatados(
                        e.partidosEmpatados
                )

                .partidosPerdidos(
                        e.partidosPerdidos
                )

                .golesFavor(
                        e.golesFavor
                )

                .golesContra(
                        e.golesContra
                )

                .diferenciaGoles(
                        diferenciaGoles
                )

                .puntos(
                        e.puntos
                )

                .build();
    }


    // =========================================================
    // ORDEN DE LA TABLA
    // =========================================================

    private Comparator<TablasPosicionResponse> comparadorTabla() {

        return Comparator

                // -------------------------------------------------
                // 1. MÁS PUNTOS
                // -------------------------------------------------

                .comparingInt(
                        TablasPosicionResponse::getPuntos
                )
                .reversed()


                // -------------------------------------------------
                // 2. MAYOR DIFERENCIA DE GOLES
                // -------------------------------------------------

                .thenComparing(
                        TablasPosicionResponse::getDiferenciaGoles,
                        Comparator.reverseOrder()
                )


                // -------------------------------------------------
                // 3. MÁS GOLES A FAVOR
                // -------------------------------------------------

                .thenComparing(
                        TablasPosicionResponse::getGolesFavor,
                        Comparator.reverseOrder()
                )


                // -------------------------------------------------
                // 4. NOMBRE DEL EQUIPO
                // -------------------------------------------------

                .thenComparing(
                        TablasPosicionResponse::getEquipo,
                        Comparator.nullsLast(
                                String.CASE_INSENSITIVE_ORDER
                        )
                );
    }


    // =========================================================
    // ASIGNAR POSICIONES
    // =========================================================

    private void asignarPosiciones(
            List<TablasPosicionResponse> tabla
    ) {

        for (int i = 0; i < tabla.size(); i++) {

            tabla.get(i).setPosicion(
                    i + 1
            );
        }
    }


    // =========================================================
    // CLASE INTERNA PARA ACUMULAR ESTADÍSTICAS
    // =========================================================

    private static class EstadisticaEquipo {

        private Long equipoId;

        private String nombreEquipo;

        private Long grupoId;

        private String nombreGrupo;

        private int partidosJugados;

        private int partidosGanados;

        private int partidosEmpatados;

        private int partidosPerdidos;

        private int golesFavor;

        private int golesContra;

        private int puntos;
    }
}