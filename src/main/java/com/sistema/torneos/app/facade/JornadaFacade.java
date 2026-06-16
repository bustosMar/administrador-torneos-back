package com.sistema.torneos.app.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;
import com.sistema.torneos.app.web.model.response.JornadaReponse;

@Component
@Transactional(readOnly = true)
public class JornadaFacade {

    @Autowired
    private EquipoEnTorneoRepository equipoEnTorneoRepository;

    public List<JornadaReponse> generarJornada(Long idTorneo) {

        // 1. traer equipos del torneo (todos los grupos)
        List<EquipoEnTorneo> equipos =
                equipoEnTorneoRepository.getByTorneo(idTorneo);

        List<JornadaReponse> resultado = new ArrayList<>();

        // 2. agrupar por grupo
        Map<Long, List<EquipoEnTorneo>> porGrupo =
                equipos.stream()
                        .collect(Collectors.groupingBy(e -> e.getGrupo().getId()));

        // 3. generar partidos por grupo
        for (Map.Entry<Long, List<EquipoEnTorneo>> entry : porGrupo.entrySet()) {

            List<EquipoEnTorneo> equiposGrupo = entry.getValue();

            for (int i = 0; i < equiposGrupo.size() - 1; i += 2) {

                EquipoEnTorneo local = equiposGrupo.get(i);
                EquipoEnTorneo visitante = equiposGrupo.get(i + 1);

                JornadaReponse model = new JornadaReponse();

                model.setTorneo(local.getTorneo().getNombre());
                model.setGrupo(local.getGrupo().getNombre());
                model.setLocal(local.getEquipo().getNombre());
                model.setVisitante(visitante.getEquipo().getNombre());
                model.setIdTorneo(local.getTorneo().getId());
                model.setIdGrupo(local.getGrupo().getId());
                model.setIdLocal(local.getId());
                model.setIdVisitante(visitante.getId());


                resultado.add(model);
            }
        }
        return resultado;
    }
   
}
    