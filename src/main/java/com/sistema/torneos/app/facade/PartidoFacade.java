package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Jornada;
import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.repository.JornadaRepository;
import com.sistema.torneos.app.domain.repository.PartidoRepository;
import com.sistema.torneos.app.exception.ResourceNotFoundException;
import com.sistema.torneos.app.web.model.PartidoModel;
import com.sistema.torneos.app.web.model.mapper.PartidoMapper;
import com.sistema.torneos.app.web.model.response.PartidoResponse;
import com.sistema.torneos.app.domain.entity.Arbitro;
import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.repository.ArbitroRepository;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;
import java.util.Optional;

import java.util.List;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class PartidoFacade {

	 private final PartidoRepository partidoRepository;
	 private final JornadaRepository jornadaRepository;
	 private final EquipoEnTorneoRepository equipoEnTorneoRepository;
	 private final ArbitroRepository arbitroRepository;
	 

	    @Autowired
	    public PartidoFacade(
	            PartidoRepository partidoRepository,
	            JornadaRepository jornadaRepository,
	            EquipoEnTorneoRepository equipoEnTorneoRepository,
	            ArbitroRepository arbitroRepository) {

	        this.partidoRepository = partidoRepository;
	        this.jornadaRepository = jornadaRepository;
	        this.equipoEnTorneoRepository = equipoEnTorneoRepository;
	        this.arbitroRepository = arbitroRepository;
	    }


    public List<PartidoModel> findAll() {
    	
    	List<Partido> partidos = partidoRepository.findAll();
    	
    	return PartidoMapper.INSTANCE.toModel(partidos);
    }

    public PartidoModel findById(Long id) {
        return PartidoMapper.INSTANCE.toModel(partidoRepository.findById(id).orElse(null));
    }

        public List<PartidoResponse> findPartidosUltimaJornadaJugada(Long idTorneo, Long idCategoria) {
                return partidoRepository
                                .findPartidosByUltimaJornadaJugadaAndCategoria(idTorneo, idCategoria)
                                .stream()
                                .map(this::mapPartidoResponse)
                                .toList();
        }

    @Transactional
    public PartidoModel create(PartidoModel partidoModel) {

        Partido partido =
                PartidoMapper.INSTANCE.toEntity(partidoModel);

        Long idTorneo =
                partido.getJornada()
                        .getTorneo()
                        .getId();

        Long idCategoria =
                partido.getEquipoLocal()
                        .getCategoriaTorneo()
                        .getId();

        Long idGrupo =
                partido.getGrupo()
                        .getId();

        Long idGrupoLocal =
                partido.getEquipoLocal()
                        .getGrupo()
                        .getId();

        Long idGrupoVisitante =
                partido.getEquipoVisitante()
                        .getGrupo()
                        .getId();

        if (!idGrupoLocal.equals(idGrupo) || !idGrupoVisitante.equals(idGrupo)) {
            throw new ResourceNotFoundException(
                    "Los equipos seleccionados no pertenecen al mismo grupo del partido."
            );
        }

        List<Partido> partidosJugados =
                partidoRepository
                        .findByJornada_Torneo_IdAndEquipoLocal_CategoriaTorneo_IdAndGrupo_Id(
                                idTorneo,
                                idCategoria,
                                idGrupo);

        Optional<Partido> partidoExistente =
                partidosJugados.stream()
                        .filter(jugado ->

                                (
                                    jugado.getGrupo().getId().equals(idGrupo)
                                    &&
                                    jugado.getEquipoLocal().getId()
                                            .equals(partido.getEquipoLocal().getId())
                                    &&
                                    jugado.getEquipoVisitante().getId()
                                            .equals(partido.getEquipoVisitante().getId())
                                )

                                ||

                                (
                                    jugado.getGrupo().getId().equals(idGrupo)
                                    &&
                                    jugado.getEquipoLocal().getId()
                                            .equals(partido.getEquipoVisitante().getId())
                                    &&
                                    jugado.getEquipoVisitante().getId()
                                            .equals(partido.getEquipoLocal().getId())
                                )
                        )
                        .findFirst();

        if (partidoExistente.isPresent()) {

            Partido repetido = partidoExistente.get();

            throw new ResourceNotFoundException(
                    String.format(
                            "El encuentro '%s vs %s' ya fue jugado en la jornada %d.",
                            repetido.getEquipoLocal().getEquipo().getNombre(),
                            repetido.getEquipoVisitante().getEquipo().getNombre(),
                            repetido.getJornada().getNumeroJornada()
                    )
            );
        }

        Partido partidoGuardado =
                partidoRepository.save(partido);

        return PartidoMapper.INSTANCE.toModel(partidoGuardado);
    }

    @Transactional
    public PartidoModel update(Long id, PartidoModel partidoModel) {
    	 if (partidoRepository.existsById(id)) {
        	 Partido partido = PartidoMapper.INSTANCE.toEntity(partidoModel);
        	 partido.setId(id);
             return PartidoMapper.INSTANCE.toModel(partidoRepository.save(partido));
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
    	if (partidoRepository.existsById(id)) {
    		partidoRepository.deleteById(id);
        }        
    }

    @Transactional
    public void createPartidos(List<PartidoModel> partidoModels) {

        List<Partido> partidos =
                PartidoMapper.INSTANCE.toEntityList(partidoModels);

        List<Jornada> jornadasActualizadas = new ArrayList<>();

        for (Partido partido : partidos) {

            Long idJornada = partido.getJornada().getId();

            Jornada jornadaBD =
                    jornadaRepository.findById(idJornada)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "No existe la jornada con id: "
                                                    + idJornada));

            Long idEquipoLocal =
                    partido.getEquipoLocal().getId();

            EquipoEnTorneo equipoLocalBD =
                    equipoEnTorneoRepository.findById(idEquipoLocal)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "No existe el equipo local con id: "
                                                    + idEquipoLocal));

            Long idEquipoVisitante =
                    partido.getEquipoVisitante().getId();

            EquipoEnTorneo equipoVisitanteBD =
                    equipoEnTorneoRepository.findById(idEquipoVisitante)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "No existe el equipo visitante con id: "
                                                    + idEquipoVisitante));

            /*
             * Solo validar encuentros repetidos cuando
             * el partido sea nuevo, es decir, cuando no tenga id.
             */
            if (partido.getId() == null) {

                Long idTorneo =
                        jornadaBD.getTorneo().getId();

                Long idCategoria =
                        equipoLocalBD
                                .getCategoriaTorneo()
                                .getId();

                Long idGrupo =
                        partido.getGrupo().getId();

                List<Partido> partidosJugados =
                        partidoRepository
                                .findByJornada_Torneo_IdAndEquipoLocal_CategoriaTorneo_IdAndGrupo_Id(
                                        idTorneo,
                                        idCategoria,
                                        idGrupo
                                );

                Optional<Partido> partidoExistente =
                        partidosJugados.stream()
                                .filter(jugado -> {

                                    boolean mismoOrden =
                                            jugado.getEquipoLocal()
                                                    .getId()
                                                    .equals(equipoLocalBD.getId())
                                            &&
                                            jugado.getEquipoVisitante()
                                                    .getId()
                                                    .equals(equipoVisitanteBD.getId());

                                    boolean ordenInvertido =
                                            jugado.getEquipoLocal()
                                                    .getId()
                                                    .equals(equipoVisitanteBD.getId())
                                            &&
                                            jugado.getEquipoVisitante()
                                                    .getId()
                                                    .equals(equipoLocalBD.getId());

                                    return mismoOrden || ordenInvertido;
                                })
                                .findFirst();

                if (partidoExistente.isPresent()) {

                    Partido repetido =
                            partidoExistente.get();

                    throw new ResourceNotFoundException(
                            String.format(
                                    "El encuentro '%s vs %s' ya fue jugado en la jornada %d.",
                                    repetido.getEquipoLocal()
                                            .getEquipo()
                                            .getNombre(),
                                    repetido.getEquipoVisitante()
                                            .getEquipo()
                                            .getNombre(),
                                    repetido.getJornada()
                                            .getNumeroJornada()
                            )
                    );
                }

                
            }

            /*
             * Se asignan las entidades obtenidas de la base de datos,
             * tanto para nuevos como para existentes.
             */
            partido.setJornada(jornadaBD);
            partido.setEquipoLocal(equipoLocalBD);
            partido.setEquipoVisitante(equipoVisitanteBD);

                        if (jornadasActualizadas.stream().noneMatch(jornada -> jornada.getId().equals(jornadaBD.getId()))) {
                                jornadasActualizadas.add(jornadaBD);
                        }

            /*
             * No poner siempre arbitro en null porque al actualizar
             * eliminarías el árbitro seleccionado.
             */
            if (partido.getArbitro() != null
                    && partido.getArbitro().getId() != null) {

                Long idArbitro =
                        partido.getArbitro().getId();

                Arbitro arbitroBD =
                        arbitroRepository.findById(idArbitro)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "No existe el árbitro con id: "
                                                        + idArbitro));

                partido.setArbitro(arbitroBD);

            } else {
                partido.setArbitro(null);
            }

          
        }

        partidoRepository.saveAll(partidos);

                if (!jornadasActualizadas.isEmpty()) {

                        for (Jornada jornada : jornadasActualizadas) {

                                switch (jornada.getEstado()) {

                                        case "PROGRAMADA":
                                                jornada.setEstado("EN_CURSO");
                                                break;

                                        case "EN_CURSO":
                                                jornada.setEstado("JUGADA");
                                                break;

                                        default:
                                                break;
                                }
                        }

                        jornadaRepository.saveAll(jornadasActualizadas);
        }
    }  

        private PartidoResponse mapPartidoResponse(Partido partido) {

                PartidoResponse dto = new PartidoResponse();

                dto.setIdPartido(partido.getId());
                dto.setIdJornada(partido.getJornada().getId());
                dto.setNumeroJornada(partido.getJornada().getNumeroJornada());
                dto.setEstado(partido.getJornada().getEstado());

                dto.setIdTorneo(partido.getJornada().getTorneo().getId());
                dto.setTorneo(partido.getJornada().getTorneo().getNombre());

                if (partido.getGrupo() != null) {
                        dto.setIdGrupo(partido.getGrupo().getId());
                        dto.setGrupo(partido.getGrupo().getNombre());
                }

                dto.setIdEquipoLocal(partido.getEquipoLocal().getId());
                dto.setEquipoLocal(partido.getEquipoLocal().getEquipo().getNombre());

                dto.setIdEquipoVisitante(partido.getEquipoVisitante().getId());
                dto.setEquipoVisitante(partido.getEquipoVisitante().getEquipo().getNombre());

                dto.setFecha(partido.getFecha() != null ? partido.getFecha().toString() : null);
                dto.setHora(partido.getHora());

                dto.setGolesLocal(0);
                dto.setGolesVisitante(0);

                if (partido.getArbitro() != null) {
                        dto.setIdArbitro(partido.getArbitro().getId());
                        dto.setArbitro(partido.getArbitro().getNombre());
                }

                return dto;
        }
   
}
