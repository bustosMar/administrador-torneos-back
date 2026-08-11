package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.entity.Gol;
import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.domain.repository.GolRepository;
import com.sistema.torneos.app.web.model.response.TablaGoleadoresResponse;
import com.sistema.torneos.app.web.model.response.TablaGoleoResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TablaGoleoFacade {

    private final GolRepository golRepository;


    // =========================================================
    // CALCULAR TABLA DE GOLEO
    // =========================================================

    public TablaGoleoResponse calcularTablaGoleo(
            Long torneoId,
            Long categoriaId
    ) {

        // =====================================================
        // OBTENER GOLES
        // =====================================================

        List<Gol> goles =
                golRepository.findGolesJugadoresPorTorneoYCategoria(
                        torneoId,
                        categoriaId
                );


        // =====================================================
        // AGRUPAR GOLES
        //
        // jugadorId -> información del jugador/equipo
        // =====================================================

        Map<Long, EstadisticaGoleador> goleadores =
                new LinkedHashMap<>();


        for (Gol gol : goles) {

            if (gol == null) {
                continue;
            }


            Jugador jugador =
                    gol.getJugador();

            EquipoEnTorneo equipo =
                    gol.getEquipoTorneo();


            if (jugador == null || equipo == null) {
                continue;
            }


            Long jugadorId =
                    jugador.getId();


            EstadisticaGoleador estadistica =
                    goleadores.computeIfAbsent(
                            jugadorId,
                            id -> crearEstadistica(
                                    jugador,
                                    equipo
                            )
                    );


            estadistica.goles++;
        }


        // =====================================================
        // ORDENAR
        //
        // 1. Más goles
        // 2. Nombre del jugador
        // =====================================================

        List<TablaGoleadoresResponse> tabla =
                goleadores.values()
                        .stream()
                        .sorted(
                                Comparator
                                        .comparingInt(
                                                (EstadisticaGoleador e) ->
                                                        e.goles
                                        )
                                        .reversed()

                                        .thenComparing(
                                                e ->
                                                        e.nombreJugador,
                                                Comparator.nullsLast(
                                                        String.CASE_INSENSITIVE_ORDER
                                                )
                                        )
                        )
                        .map(this::convertirDTO)
                        .collect(Collectors.toList());


        // =====================================================
        // ASIGNAR POSICIONES
        // =====================================================

        asignarPosiciones(tabla);


        // =====================================================
        // RESPUESTA FINAL
        // =====================================================

        return TablaGoleoResponse.builder()
                .goleadores(tabla)
                .build();
    }


    // =========================================================
    // CREAR ESTADÍSTICA
    // =========================================================

    private EstadisticaGoleador crearEstadistica(
            Jugador jugador,
            EquipoEnTorneo equipo
    ) {

        EstadisticaGoleador estadistica =
                new EstadisticaGoleador();


        // =====================================================
        // JUGADOR
        // =====================================================

        estadistica.jugadorId =
                jugador.getId();


        /*
         * Aquí ajusta los getters según tu entidad Jugador.
         */

        estadistica.nombreJugador =
                construirNombreJugador(jugador);


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


        return estadistica;
    }


    // =========================================================
    // CONSTRUIR NOMBRE DEL JUGADOR
    // =========================================================

    private String construirNombreJugador(
            Jugador jugador
    ) {

        String nombre =
                jugador.getNombre();

        String apellido =
                jugador.getApellido();


        if (
                nombre == null &&
                apellido == null
        ) {

            return "Jugador sin nombre";
        }


        if (nombre == null) {
            return apellido;
        }


        if (apellido == null) {
            return nombre;
        }


        return nombre + " " + apellido;
    }


    // =========================================================
    // CONVERTIR DTO
    // =========================================================

    private TablaGoleadoresResponse convertirDTO(
            EstadisticaGoleador e
    ) {

        return TablaGoleadoresResponse.builder()

                .posicion(
                        0
                )

                .jugadorId(
                        e.jugadorId
                )

                .jugador(
                        e.nombreJugador
                )

                .equipoId(
                        e.equipoId
                )

                .equipo(
                        e.nombreEquipo
                )

                .goles(
                        e.goles
                )

                .build();
    }


    // =========================================================
    // POSICIONES
    // =========================================================

    private void asignarPosiciones(
            List<TablaGoleadoresResponse> tabla
    ) {

        for (
                int i = 0;
                i < tabla.size();
                i++
        ) {

            tabla.get(i).setPosicion(
                    i + 1
            );
        }
    }


    // =========================================================
    // CLASE INTERNA
    // =========================================================

    private static class EstadisticaGoleador {

        private Long jugadorId;

        private String nombreJugador;

        private Long equipoId;

        private String nombreEquipo;

        private int goles;
    }
}