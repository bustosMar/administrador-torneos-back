package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.*;
import com.sistema.torneos.app.domain.repository.*;
import com.sistema.torneos.app.web.model.response.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
public class JornadaFacade {

    @Autowired
    private JornadaRepository jornadaRepository;
    
    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private EquipoEnTorneoRepository equipoEnTorneoRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    private LocalDate ultimoDomingo;

    // =====================================================
    // 1. GENERAR CALENDARIO (SOLO JORNADAS)
    // =====================================================
    public List<JornadaResponse> crearJornadas(Long idTorneo) {

        // 1. Traer todos los equipos del torneo
        List<EquipoEnTorneo> equipos =
                equipoEnTorneoRepository.getByTorneo(idTorneo);

        if (equipos == null || equipos.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Agrupar por grupo (porque el grupo viene desde EquipoEnTorneo)
        Map<Grupo, List<EquipoEnTorneo>> porGrupo =
                equipos.stream()
                        .collect(Collectors.groupingBy(EquipoEnTorneo::getGrupo));

        List<Jornada> nuevasJornadas = new ArrayList<>();

        // 3. Iterar por cada grupo
        for (Map.Entry<Grupo, List<EquipoEnTorneo>> entry : porGrupo.entrySet()) {

            Grupo grupo = entry.getKey();
            List<EquipoEnTorneo> equiposGrupo = entry.getValue();
            
            System.out.println("GRUPO = " + grupo);
            System.out.println("ID = " + grupo.getId());

            if (equiposGrupo == null || equiposGrupo.size() < 2) {
                continue;
            }

            // 4. Generar calendario round robin por grupo
            List<List<EquipoEnTorneo>> calendario =
                    generarRoundRobin(equiposGrupo);

            // 5. Número de jornada (continuación por grupo)
            int siguienteNumero =
                    obtenerUltimoNumeroJornada(grupo.getId()) + 1;

            // 6. Torneo (tomado desde cualquier equipo del grupo)
            Torneo torneo =
                    equiposGrupo.get(0).getTorneo();

            // 7. Crear jornadas
            for (List<EquipoEnTorneo> ronda : calendario) {

                Jornada j = new Jornada();

                j.setNumeroJornada(siguienteNumero++);
                j.setEstado("PROGRAMADA");
                j.setTorneo(torneo);
                j.setGrupo(grupo);
                j.setFechaProgramada(calcularSiguienteDomingo());

                nuevasJornadas.add(j);
            }
        }

        // 8. Guardar todo
        jornadaRepository.saveAll(nuevasJornadas);

        // 9. Mapear respuesta
        return nuevasJornadas.stream()
                .map(this::mapJornadaSinPartidos)
                .toList();
    }
 
    
    // =====================================================
    // FECHA SIGUIENTE DOMINGO (SIN ESTADO GLOBAL)
    // =====================================================
    private LocalDate calcularSiguienteDomingo() {
        return LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
    }

    // =====================================================
    // OBTENER ÚLTIMO NÚMERO DE JORNADA
    // =====================================================
    private int obtenerUltimoNumeroJornada(Long id) {

    	 Jornada ultima =
    	            jornadaRepository.findTopByGrupoIdOrderByNumeroJornadaDesc(
    	                    id);

    	    return ultima != null
    	            ? ultima.getNumeroJornada()
    	            : 0;
    }

    // =====================================================
    // VALIDAR SI YA EXISTE JORNADA
    // =====================================================
    private boolean existeJornada(Long grupoId, int numero) {

        return jornadaRepository
                .existsByGrupoIdAndNumeroJornada(grupoId, numero);
    }

    // =====================================================
    // MAP SIMPLE
    // =====================================================
    private JornadaResponse mapJornadaSinPartidos(Jornada j) {

        JornadaResponse dto = new JornadaResponse();

        dto.setIdJornada(j.getId());
        dto.setNumeroJornada(j.getNumeroJornada());
        dto.setEstado(j.getEstado());

        dto.setIdTorneo(j.getTorneo().getId());
        dto.setTorneo(j.getTorneo().getNombre());

        if (j.getGrupo() != null) {
            dto.setIdGrupo(j.getGrupo().getId());
            dto.setGrupo(j.getGrupo().getNombre());
        }

        dto.setFechaProgramada(j.getFechaProgramada());

        dto.setPartidos(new ArrayList<>());

        return dto;
    }


    // =====================================================
    // 2. ACTIVAR SIGUIENTE JORNADA (CREA PARTIDOS)
    // =====================================================
    public JornadaResponse activarSiguienteJornada(Long idTorneo) {

        Jornada jornada = jornadaRepository
                .findFirstByTorneoIdAndEstado(idTorneo, "PROGRAMADA");

        if (jornada == null) {
            throw new RuntimeException("No hay jornadas disponibles");
        }

        List<EquipoEnTorneo> equipos =
                equipoEnTorneoRepository.getByTorneo(idTorneo);

        List<List<EquipoEnTorneo>> calendario =
                generarRoundRobin(equipos);

        List<EquipoEnTorneo> ronda =
                calendario.get(jornada.getNumeroJornada() - 1);

        List<Partido> partidos = new ArrayList<>();

        for (int i = 0; i < ronda.size(); i += 2) {

            EquipoEnTorneo local = ronda.get(i);
            EquipoEnTorneo visitante = ronda.get(i + 1);

            if (local == null || visitante == null) {
                continue;
            }

            Partido p = new Partido();
            p.setJornada(jornada);
            p.setEquipoLocal(local);
            p.setEquipoVisitante(visitante);
            p.setGrupo(local.getGrupo());

            p.setFecha(jornada.getFechaProgramada());
            p.setHora("19:00"); // puedes parametrizarlo
            p.setGoles(new HashSet<>());
            p.setPresencias(new HashSet<>());

            partidos.add(p);
        }

        partidoRepository.saveAll(partidos);

        jornada.setEstado("EN_CURSO");
        jornadaRepository.save(jornada);

        return mapJornada(jornada, partidos);
    }

    // =====================================================
    // 3. OBTENER JORNADA ACTUAL
    // =====================================================
    public JornadaResponse obtenerJornadaActual(Long idTorneo) {

        Jornada jornada = jornadaRepository
                .findFirstByTorneoIdAndEstado(idTorneo, "EN_CURSO");

        if (jornada == null) {
            return null;
        }

        List<Partido> partidos =
                partidoRepository.findByJornadaId(jornada.getId());

        return mapJornada(jornada, partidos);
    }

    // =====================================================
    // 4. ROUND ROBIN
    // =====================================================
    private List<List<EquipoEnTorneo>> generarRoundRobin(List<EquipoEnTorneo> equipos) {

        List<EquipoEnTorneo> lista = new ArrayList<>(equipos);

        if (lista.size() % 2 != 0) {
            lista.add(null);
        }

        int n = lista.size();
        int rondas = n - 1;

        List<List<EquipoEnTorneo>> jornadas = new ArrayList<>();

        for (int r = 0; r < rondas; r++) {

            List<EquipoEnTorneo> ronda = new ArrayList<>();

            for (int i = 0; i < n / 2; i++) {

                EquipoEnTorneo local = lista.get(i);
                EquipoEnTorneo visitante = lista.get(n - 1 - i);

                ronda.add(local);
                ronda.add(visitante);
            }

            jornadas.add(ronda);

            // rotación
            EquipoEnTorneo fijo = lista.get(0);
            List<EquipoEnTorneo> rotacion =
                    new ArrayList<>(lista.subList(1, lista.size()));

            EquipoEnTorneo ultimo = rotacion.remove(rotacion.size() - 1);
            rotacion.add(0, ultimo);

            lista.clear();
            lista.add(fijo);
            lista.addAll(rotacion);
        }

        return jornadas;
    }

    // =====================================================
    // 5. MAP JORNADA CON PARTIDOS
    // =====================================================
    private JornadaResponse mapJornada(Jornada jornada, List<Partido> partidos) {

        JornadaResponse dto = new JornadaResponse();

        dto.setIdJornada(jornada.getId());
        dto.setNumeroJornada(jornada.getNumeroJornada());
        dto.setEstado(jornada.getEstado());

        dto.setIdTorneo(jornada.getTorneo().getId());
        dto.setTorneo(jornada.getTorneo().getNombre());

        if (jornada.getGrupo() != null) {
            dto.setIdGrupo(jornada.getGrupo().getId());
            dto.setGrupo(jornada.getGrupo().getNombre());
        }

        dto.setFechaProgramada(jornada.getFechaProgramada());

        dto.setPartidos(
                partidos.stream()
                        .map(this::mapPartido)
                        .toList()
        );

        return dto;
    }


    // =====================================================
    // 7. MAP PARTIDO
    // =====================================================
    private PartidoResponse mapPartido(Partido p) {

        PartidoResponse dto = new PartidoResponse();

        dto.setIdPartido(p.getId());

        dto.setIdJornada(p.getJornada().getId());
        dto.setNumeroJornada(p.getJornada().getNumeroJornada());

        dto.setEstado("PENDIENTE");

        dto.setIdTorneo(p.getJornada().getTorneo().getId());
        dto.setTorneo(p.getJornada().getTorneo().getNombre());

        if (p.getGrupo() != null) {
            dto.setIdGrupo(p.getGrupo().getId());
            dto.setGrupo(p.getGrupo().getNombre());
        }

        dto.setIdEquipoLocal(p.getEquipoLocal().getId());
        dto.setEquipoLocal(p.getEquipoLocal().getEquipo().getNombre());

        dto.setIdEquipoVisitante(p.getEquipoVisitante().getId());
        dto.setEquipoVisitante(p.getEquipoVisitante().getEquipo().getNombre());

        dto.setFecha(p.getFecha() != null ? p.getFecha().toString() : null);
        dto.setHora(p.getHora());

        dto.setGolesLocal(0);
        dto.setGolesVisitante(0);

        if (p.getArbitro() != null) {
            dto.setIdArbitro(p.getArbitro().getId());
            dto.setArbitro(p.getArbitro().getNombre());
        }

        return dto;
    }
    
 
}