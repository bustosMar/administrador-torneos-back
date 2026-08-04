package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.*;
import com.sistema.torneos.app.domain.repository.*;
import com.sistema.torneos.app.web.model.JugadorModel;
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
    private PartidoRepository partidoRepository;

    @Autowired
    private EquipoEnTorneoRepository equipoEnTorneoRepository;

 
    // =====================================================
    // 1. GENERAR CALENDARIO (SOLO JORNADAS)
    // =====================================================
    @Transactional
    public List<JornadaResponse> crearJornadas(Long idTorneo, Long idCategoria) {

        List<EquipoEnTorneo> equipos =
                equipoEnTorneoRepository.findByTorneo_IdAndCategoriaTorneo_Id(
                        idTorneo,
                        idCategoria);

        if (equipos == null || equipos.isEmpty()) {
            return new ArrayList<>();
        }

        Set<Long> gruposCategoria =
            equipos.stream()
                .map(EquipoEnTorneo::getGrupo)
                .filter(Objects::nonNull)
                .map(Grupo::getId)
                .collect(Collectors.toSet());

        boolean existeJornadaEnCurso =
            jornadaRepository.findByTorneoIdAndEstado(idTorneo, "EN_CURSO")
                .stream()
                .filter(jornada -> jornada.getGrupo() != null)
                .anyMatch(jornada -> gruposCategoria.contains(jornada.getGrupo().getId()));

        if (existeJornadaEnCurso) {
            throw new RuntimeException("No puedes generar una nueva jornada, porque hay una en curso");
        }

        List<Jornada> jornadasExistentes =
                jornadaRepository.findByTorneoId(idTorneo);

        Map<Grupo, List<EquipoEnTorneo>> porGrupo =
                equipos.stream()
                        .collect(Collectors.groupingBy(EquipoEnTorneo::getGrupo));

        List<Jornada> nuevasJornadas = new ArrayList<>();
        List<Jornada> jornadasActualizadas = new ArrayList<>();

        for (Map.Entry<Grupo, List<EquipoEnTorneo>> entry : porGrupo.entrySet()) {

            Grupo grupo = entry.getKey();
            List<EquipoEnTorneo> equiposGrupo = entry.getValue();

            if (equiposGrupo.size() < 2) {
                continue;
            }

            Torneo torneo = equiposGrupo.get(0).getTorneo();

            List<List<EquipoEnTorneo>> calendario =
                    generarRoundRobin(equiposGrupo);

            int totalJornadasNecesarias = calendario.size();

            List<Jornada> jornadasGrupo =
                    jornadasExistentes.stream()
                            .filter(j -> j.getGrupo() != null)
                            .filter(j -> j.getGrupo().getId().equals(grupo.getId()))
                            .toList();

            for (Jornada jornadaExistente : jornadasGrupo) {
                if (!"PROGRAMADA".equalsIgnoreCase(jornadaExistente.getEstado())) {
                    continue;
                }

                LocalDate fechaEsperada =
                        calcularFechaJornada(jornadaExistente.getNumeroJornada());

                if (!Objects.equals(jornadaExistente.getFechaProgramada(), fechaEsperada)) {
                    jornadaExistente.setFechaProgramada(fechaEsperada);
                    jornadasActualizadas.add(jornadaExistente);
                }
            }

            Set<Integer> numerosExistentes =
                    jornadasGrupo.stream()
                            .map(Jornada::getNumeroJornada)
                            .collect(Collectors.toSet());

            for (int numeroJornada = 1;
                 numeroJornada <= totalJornadasNecesarias;
                 numeroJornada++) {

                if (numerosExistentes.contains(numeroJornada)) {
                    continue;
                }

                Jornada jornada = new Jornada();

                jornada.setNumeroJornada(numeroJornada);
                jornada.setEstado("PROGRAMADA");
                jornada.setTorneo(torneo);
                jornada.setGrupo(grupo);
                jornada.setFechaProgramada(calcularFechaJornada(numeroJornada));

                nuevasJornadas.add(jornada);
            }
        }

        if (!nuevasJornadas.isEmpty()) {
            jornadaRepository.saveAll(nuevasJornadas);
        }

        if (!jornadasActualizadas.isEmpty()) {
            jornadaRepository.saveAll(jornadasActualizadas);
        }

        List<Jornada> resultado =
                jornadaRepository.findByTorneoId(idTorneo)
                        .stream()
                        .filter(j -> "PROGRAMADA".equalsIgnoreCase(j.getEstado()))
                        .toList();

        return resultado.stream()
                .map(this::mapJornadaSinPartidos)
                .toList();
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
    // 3. OBTENER JORNADA ACTUAL
    // =====================================================
    public JornadaResponse obtenerJornadaActual(Long idTorneo, Long idCategoria) {

        List<EquipoEnTorneo> equiposCategoria =
            equipoEnTorneoRepository.findByTorneo_IdAndCategoriaTorneo_Id(idTorneo, idCategoria);

        if (equiposCategoria == null || equiposCategoria.isEmpty()) {
            return null;
        }

        Set<Long> gruposCategoria = equiposCategoria.stream()
            .map(EquipoEnTorneo::getGrupo)
            .filter(Objects::nonNull)
            .map(Grupo::getId)
            .collect(Collectors.toSet());

        List<Jornada> jornadasEnCurso = jornadaRepository.findByTorneoIdAndEstado(idTorneo, "EN_CURSO")
            .stream()
            .filter(jornada -> jornada.getGrupo() != null)
            .filter(jornada -> gruposCategoria.contains(jornada.getGrupo().getId()))
            .sorted(Comparator
                .comparing(Jornada::getNumeroJornada)
                .thenComparing(Jornada::getId))
            .toList();

        if (jornadasEnCurso.isEmpty()) {
            return null;
        }

        Jornada jornadaPrincipal = jornadasEnCurso.get(0);

        List<Partido> partidos = jornadasEnCurso.stream()
            .flatMap(jornada -> partidoRepository.findByJornadaId(jornada.getId()).stream())
            .toList();

        return mapJornada(jornadaPrincipal, partidos, idCategoria);
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
    private JornadaResponse mapJornada(
            Jornada jornada,
            List<Partido> partidos,
            Long idCategoria) {

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
        
        if(idCategoria!=null) {
        	
        dto.setPartidos(
                partidos.stream()
                        .filter(partido ->
                                partido.getEquipoLocal() != null
                                &&
                                partido.getEquipoLocal().getCategoriaTorneo() != null
                                &&
                                partido.getEquipoLocal().getCategoriaTorneo().getCategoria() != null
                                &&
                                partido.getEquipoLocal()
                                        .getCategoriaTorneo()
                                        .getCategoria()
                                        .getId()
                                        .equals(idCategoria)
                        )
                        .map(this::mapPartido)
                        .toList()
        );
        
        }else {
        	dto.setPartidos( partidos.stream() .map(this::mapPartido) .toList() );
    	}

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
    
    @Transactional
    public void reajustarSecuencia() {   

        jornadaRepository.sincronizarSecuencia();
    }
    
    private boolean existePartido(
            EquipoEnTorneo local,
            EquipoEnTorneo visitante,
            Set<String> partidosExistentes) {

        Long id1 = local.getId();
        Long id2 = visitante.getId();

        String clave =
                Math.min(id1, id2)
                + "-"
                + Math.max(id1, id2);

        return partidosExistentes.contains(clave);
    }
    
    private List<EquipoEnTorneo> generarRondaSinRepetidos(
            List<EquipoEnTorneo> equipos,
            Set<String> partidosExistentes) {

        List<EquipoEnTorneo> ronda = new ArrayList<>();

        Set<Long> usados = new HashSet<>();

        for (int i = 0; i < equipos.size(); i++) {

            EquipoEnTorneo local = equipos.get(i);

            if (usados.contains(local.getId())) {
                continue;
            }

            for (int j = i + 1; j < equipos.size(); j++) {

                EquipoEnTorneo visitante = equipos.get(j);

                if (usados.contains(visitante.getId())) {
                    continue;
                }

                if (!existePartido(
                        local,
                        visitante,
                        partidosExistentes)) {

                    ronda.add(local);
                    ronda.add(visitante);

                    usados.add(local.getId());
                    usados.add(visitante.getId());

                    String clave =
                            Math.min(local.getId(), visitante.getId())
                            + "-"
                            + Math.max(local.getId(), visitante.getId());

                    partidosExistentes.add(clave);

                    break;
                }
            }
        }

        return ronda;
    }
    
    private LocalDate calcularFechaJornada(int semanas) {

        LocalDate primerDomingo =
                LocalDate.now()
                        .with(
                            TemporalAdjusters.next(
                                    DayOfWeek.SUNDAY));

        return primerDomingo.plusWeeks(Math.max(semanas - 1, 0));
    }
    
 // =====================================================
 // PREVISUALIZAR SIGUIENTE JORNADA SIN GUARDAR PARTIDOS
 // =====================================================
 @Transactional(readOnly = true)
 public JornadaResponse previsualizarSiguienteJornada(Long idTorneo, Long idCategoria) {

     List<EquipoEnTorneo> equipos =
         equipoEnTorneoRepository.findByTorneo_IdAndCategoriaTorneo_Id(idTorneo, idCategoria);

     if (equipos == null || equipos.isEmpty()) {
     throw new RuntimeException("No hay equipos disponibles para la categoría seleccionada");
     }

     Map<Grupo, List<EquipoEnTorneo>> equiposPorGrupo =
         equipos.stream()
             .filter(equipo -> equipo.getGrupo() != null)
             .collect(Collectors.groupingBy(EquipoEnTorneo::getGrupo));

     List<Jornada> jornadasProgramadas =
         jornadaRepository.findByTorneoIdAndEstado(idTorneo, "PROGRAMADA")
             .stream()
             .filter(jornada -> jornada.getGrupo() != null)
             .filter(jornada -> equiposPorGrupo.keySet().stream()
                 .anyMatch(grupo -> grupo.getId().equals(jornada.getGrupo().getId())))
             .toList();

     if (jornadasProgramadas.isEmpty()) {
     throw new RuntimeException("No hay jornadas disponibles");
     }

     Map<Long, Jornada> siguienteJornadaPorGrupo =
         jornadasProgramadas.stream()
             .collect(Collectors.toMap(
                 jornada -> jornada.getGrupo().getId(),
                 jornada -> jornada,
                 (jornada1, jornada2) -> jornada1.getNumeroJornada() <= jornada2.getNumeroJornada()
                     ? jornada1
                     : jornada2
             ));

     List<Partido> partidosExistentesBD = partidoRepository.findAll();
     List<Partido> partidos = new ArrayList<>();

     for (Map.Entry<Grupo, List<EquipoEnTorneo>> entry : equiposPorGrupo.entrySet()) {

     Grupo grupo = entry.getKey();
     List<EquipoEnTorneo> equiposGrupo = entry.getValue();

     Jornada jornadaGrupo = siguienteJornadaPorGrupo.get(grupo.getId());

     if (jornadaGrupo == null || equiposGrupo.size() < 2) {
         continue;
     }

     Set<String> partidosExistentesGrupo =
         partidosExistentesBD.stream()
             .filter(partido -> partido.getGrupo() != null)
             .filter(partido -> partido.getGrupo().getId().equals(grupo.getId()))
             .filter(partido -> partido.getEquipoLocal() != null)
             .filter(partido -> partido.getEquipoLocal().getCategoriaTorneo() != null)
             .filter(partido -> partido.getEquipoLocal().getCategoriaTorneo().getId().equals(idCategoria))
             .map(p -> {
                 Long a = p.getEquipoLocal().getId();
                 Long b = p.getEquipoVisitante().getId();

                 return Math.min(a, b) + "-" + Math.max(a, b);
             })
             .collect(Collectors.toSet());

     List<EquipoEnTorneo> ronda = generarRondaSinRepetidos(equiposGrupo, partidosExistentesGrupo);

     for (int i = 0; i < ronda.size(); i += 2) {

         EquipoEnTorneo local = ronda.get(i);
         EquipoEnTorneo visitante = ronda.get(i + 1);

         if (local == null || visitante == null) {
         continue;
         }

         Partido p = new Partido();

         p.setJornada(jornadaGrupo);
         p.setEquipoLocal(local);
         p.setEquipoVisitante(visitante);
         p.setGrupo(grupo);
         p.setFecha(jornadaGrupo.getFechaProgramada());
         p.setHora("09:00");
         p.setGoles(new HashSet<>());
         p.setPresencias(new HashSet<>());

         partidos.add(p);
     }
     }

     Jornada jornadaPrincipal = siguienteJornadaPorGrupo.values().stream()
         .sorted(Comparator
             .comparing(Jornada::getNumeroJornada)
             .thenComparing(Jornada::getId))
         .findFirst()
         .orElseThrow(() -> new RuntimeException("No hay jornadas disponibles"));

     return mapJornada(jornadaPrincipal, partidos, null);
 }



@SuppressWarnings("deprecation")
public void update(Long id) {
	if (jornadaRepository.existsById(id)) {
		Jornada jornada = jornadaRepository.getById(id);
		jornada.setId(1L);
		jornada.setEstado("JUGADA");		
	}
}
 
}