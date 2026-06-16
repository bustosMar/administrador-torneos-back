package com.sistema.torneos.app.facade;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;
import com.sistema.torneos.app.domain.repository.PartidoRepository;
import com.sistema.torneos.app.web.model.response.JornadaReponse;

@Component
@Transactional(readOnly = true)
public class JornadaFacade {

    @Autowired
    private EquipoEnTorneoRepository equipoEnTorneoRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    public List<JornadaReponse> generarJornada(Long idTorneo) {

        List<EquipoEnTorneo> equipos =
                equipoEnTorneoRepository.getByTorneo(idTorneo);

        List<JornadaReponse> resultado = new ArrayList<>();

        Map<Long, List<EquipoEnTorneo>> porGrupo =
                equipos.stream()
                        .collect(Collectors.groupingBy(e -> e.getGrupo().getId()));

        for (Map.Entry<Long, List<EquipoEnTorneo>> entry : porGrupo.entrySet()) {

            Long idGrupo = entry.getKey();

            List<EquipoEnTorneo> equiposGrupo =
                    new ArrayList<>(entry.getValue());

            List<Partido> partidosJugados =
                    partidoRepository.findByTorneoIdAndGrupoId(idTorneo, idGrupo);

            Set<String> enfrentamientosJugados = new HashSet<>();

            for (Partido p : partidosJugados) {
                Long l = p.getEquipoLocal().getId();
                Long v = p.getEquipoVisitante().getId();

                enfrentamientosJugados.add(l + "-" + v);
                enfrentamientosJugados.add(v + "-" + l);
            }

            Collections.shuffle(equiposGrupo);

            List<Emparejamiento> jornadaActual = new ArrayList<>();
            List<Emparejamiento> mejorJornada = new ArrayList<>();

            Set<Long> equiposDobleJornada =
                    obtenerEquiposDobleJornada(equiposGrupo, partidosJugados);

            Map<Long, Integer> partidosEnJornada = new HashMap<>();

            generarJornadaBacktracking(
                    equiposGrupo,
                    enfrentamientosJugados,
                    jornadaActual,
                    mejorJornada,
                    equiposDobleJornada,
                    partidosEnJornada
            );

            if (mejorJornada.isEmpty()) continue;

            for (Emparejamiento e : mejorJornada) {

                JornadaReponse model = new JornadaReponse();

                model.setTorneo(e.local.getTorneo().getNombre());
                model.setGrupo(e.local.getGrupo().getNombre());
                model.setLocal(e.local.getEquipo().getNombre());
                model.setVisitante(e.visitante.getEquipo().getNombre());

                model.setIdTorneo(e.local.getTorneo().getId());
                model.setIdGrupo(e.local.getGrupo().getId());
                model.setIdLocal(e.local.getId());
                model.setIdVisitante(e.visitante.getId());

                resultado.add(model);
            }
        }

        return resultado;
    }

    private void generarJornadaBacktracking(
            List<EquipoEnTorneo> disponibles,
            Set<String> enfrentamientosJugados,
            List<Emparejamiento> jornadaActual,
            List<Emparejamiento> mejorJornada,
            Set<Long> equiposDobleJornada,
            Map<Long, Integer> partidosEnJornada) {

        if (jornadaActual.size() > mejorJornada.size()) {
            mejorJornada.clear();
            mejorJornada.addAll(
                    jornadaActual.stream()
                            .map(e -> new Emparejamiento(e.local, e.visitante))
                            .collect(Collectors.toList())
            );
        }

        if (disponibles.isEmpty()) return;

        EquipoEnTorneo local = disponibles.get(0);

        List<EquipoEnTorneo> candidatos =
                new ArrayList<>(disponibles.subList(1, disponibles.size()));

        Collections.shuffle(candidatos);

        for (EquipoEnTorneo visitante : candidatos) {

            int maxLocal = equiposDobleJornada.contains(local.getId()) ? 2 : 1;
            int maxVisitante = equiposDobleJornada.contains(visitante.getId()) ? 2 : 1;

            if (partidosEnJornada.getOrDefault(local.getId(), 0) >= maxLocal) continue;
            if (partidosEnJornada.getOrDefault(visitante.getId(), 0) >= maxVisitante) continue;

            String clave = local.getId() + "-" + visitante.getId();

            if (enfrentamientosJugados.contains(clave)) continue;

            Emparejamiento emp = new Emparejamiento(local, visitante);

            // 👉 APPLY
            jornadaActual.add(emp);

            partidosEnJornada.merge(local.getId(), 1, Integer::sum);
            partidosEnJornada.merge(visitante.getId(), 1, Integer::sum);

            List<EquipoEnTorneo> restantes = new ArrayList<>(disponibles);
            restantes.remove(local);
            restantes.remove(visitante);

            generarJornadaBacktracking(
                    restantes,
                    enfrentamientosJugados,
                    jornadaActual,
                    mejorJornada,
                    equiposDobleJornada,
                    partidosEnJornada
            );

            // 👉 BACKTRACK (ESTO ES LO CRÍTICO)
            jornadaActual.remove(jornadaActual.size() - 1);

            partidosEnJornada.merge(local.getId(), -1, Integer::sum);
            partidosEnJornada.merge(visitante.getId(), -1, Integer::sum);
        }

        // 👉 DESCANSO (local sin emparejar)
        List<EquipoEnTorneo> restantes = new ArrayList<>(disponibles);
        restantes.remove(local);

        generarJornadaBacktracking(
                restantes,
                enfrentamientosJugados,
                jornadaActual,
                mejorJornada,
                equiposDobleJornada,
                partidosEnJornada
        );
    }

    private static class Emparejamiento {
        private final EquipoEnTorneo local;
        private final EquipoEnTorneo visitante;

        public Emparejamiento(EquipoEnTorneo local, EquipoEnTorneo visitante) {
            this.local = local;
            this.visitante = visitante;
        }
    }

    private int contarPartidosJugados(Long equipoId, List<Partido> partidosJugados) {
        int total = 0;

        for (Partido p : partidosJugados) {
            if (p.getEquipoLocal().getId().equals(equipoId)
                    || p.getEquipoVisitante().getId().equals(equipoId)) {
                total++;
            }
        }
        return total;
    }

    private Set<Long> obtenerEquiposDobleJornada(
            List<EquipoEnTorneo> equiposGrupo,
            List<Partido> partidosJugados) {

        Map<Long, Integer> jugados = new HashMap<>();
        int max = 0;

        for (EquipoEnTorneo e : equiposGrupo) {
            int t = contarPartidosJugados(e.getId(), partidosJugados);
            jugados.put(e.getId(), t);
            max = Math.max(max, t);
        }

        Set<Long> resultado = new HashSet<>();

        for (Map.Entry<Long, Integer> e : jugados.entrySet()) {
            if (max - e.getValue() >= 2) {
                resultado.add(e.getKey());
            }
        }

        return resultado;
    }
}