package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Jornada;
import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.repository.JornadaRepository;
import com.sistema.torneos.app.domain.repository.PartidoRepository;
import com.sistema.torneos.app.exception.ResourceNotFoundException;
import com.sistema.torneos.app.web.model.PartidoModel;
import com.sistema.torneos.app.web.model.mapper.PartidoMapper;
import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;
import java.util.Optional;

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

	    @Autowired
	    public PartidoFacade(
	            PartidoRepository partidoRepository,
	            JornadaRepository jornadaRepository,
	            EquipoEnTorneoRepository equipoEnTorneoRepository) {

	        this.partidoRepository = partidoRepository;
	        this.jornadaRepository = jornadaRepository;
	        this.equipoEnTorneoRepository = equipoEnTorneoRepository;
	    }


    public List<PartidoModel> findAll() {
    	
    	List<Partido> partidos = partidoRepository.findAll();
    	
    	return PartidoMapper.INSTANCE.toModel(partidos);
    }

    public PartidoModel findById(Long id) {
        return PartidoMapper.INSTANCE.toModel(partidoRepository.findById(id).orElse(null));
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
    public void createPartido(List<PartidoModel> partidoModels) {

        List<Partido> partidos =
                PartidoMapper.INSTANCE.toEntityList(partidoModels);

        for (Partido partido : partidos) {

            Long idJornada = partido.getJornada().getId();

            Jornada jornadaBD =
                    jornadaRepository.findById(idJornada)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "No existe la jornada con id: " + idJornada));

            EquipoEnTorneo equipoLocalBD =
                    equipoEnTorneoRepository.findById(partido.getEquipoLocal().getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "No existe el equipo local con id: "
                                                    + partido.getEquipoLocal().getId()));

            EquipoEnTorneo equipoVisitanteBD =
                    equipoEnTorneoRepository.findById(partido.getEquipoVisitante().getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "No existe el equipo visitante con id: "
                                                    + partido.getEquipoVisitante().getId()));

            Long idTorneo = jornadaBD.getTorneo().getId();
            Long idCategoria = equipoLocalBD.getCategoriaTorneo().getId();
            Long idGrupo = partido.getGrupo().getId();

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
                                        jugado.getEquipoLocal().getId()
                                                .equals(equipoLocalBD.getId())
                                        &&
                                        jugado.getEquipoVisitante().getId()
                                                .equals(equipoVisitanteBD.getId())
                                    )
                                    ||
                                    (
                                        jugado.getEquipoLocal().getId()
                                                .equals(equipoVisitanteBD.getId())
                                        &&
                                        jugado.getEquipoVisitante().getId()
                                                .equals(equipoLocalBD.getId())
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

            partido.setJornada(jornadaBD);
            partido.setEquipoLocal(equipoLocalBD);
            partido.setEquipoVisitante(equipoVisitanteBD);
            partido.setArbitro(null);
            partido.setJugado(false);
        }
                       
        partidoRepository.saveAll(partidos);
        
        if (!partidos.isEmpty()) {
            Jornada jornada = partidos.get(0).getJornada();
            jornada.setEstado("EN_CURSO");
            jornadaRepository.save(jornada);
        }
    }
}
