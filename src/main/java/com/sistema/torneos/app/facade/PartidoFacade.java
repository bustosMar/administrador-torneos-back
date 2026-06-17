package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.repository.PartidoRepository;
import com.sistema.torneos.app.exception.ResourceNotFoundException;
import com.sistema.torneos.app.web.model.mapper.PartidoMapper;
import com.sistema.torneos.app.web.model.request.PartidoRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class PartidoFacade {

    private final PartidoRepository partidoRepository;

    @Autowired
    public PartidoFacade(PartidoRepository partidoRepository) {
        this.partidoRepository = partidoRepository;
    }

    public List<Partido> findAll() {
        return partidoRepository.findAll();
    }

    public Partido findById(Long id) {
        return partidoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Partido create(Partido partido) {
        return partidoRepository.save(partido);
    }

    @Transactional
    public Partido update(Long id, Partido partido) {
        if (partidoRepository.existsById(id)) {
            partido.setId(id);
            return partidoRepository.save(partido);
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
    public void createPartido(List<PartidoRequest> request) {

        /*List<Partido> partidos =
                PartidoMapper.INSTANCE.toEntityList(request);

        for (Partido partido : partidos) {

          
            Long idGrupo = partido.getGrupo().getId();

            List<Partido> partidosJugados =
                    partidoRepository.findByJornada_Torneo_IdAndGrupo_Id(
                            idTorneo,
                            idGrupo);

            boolean existe = partidosJugados.stream().anyMatch(jugado ->

                    (jugado.getEquipoLocal().getId()
                            .equals(partido.getEquipoLocal().getId())
                     &&
                     jugado.getEquipoVisitante().getId()
                            .equals(partido.getEquipoVisitante().getId()))

                    ||

                    (jugado.getEquipoLocal().getId()
                            .equals(partido.getEquipoVisitante().getId())
                     &&
                     jugado.getEquipoVisitante().getId()
                            .equals(partido.getEquipoLocal().getId()))
            );

            if (existe) {
                throw new ResourceNotFoundException(
                        "El encuentro ya fue jugado anteriormente.");
           }
        }

        partidoRepository.saveAll(partidos);
    */}
}
