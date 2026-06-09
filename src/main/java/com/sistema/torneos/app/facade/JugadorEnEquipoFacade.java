package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.JugadorEnEquipo;
import com.sistema.torneos.app.domain.repository.JugadorEnEquipoRepository;
import com.sistema.torneos.app.web.model.JugadorEnEquipoModel;
import com.sistema.torneos.app.web.model.mapper.JugadorEnEquipoMapper;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class JugadorEnEquipoFacade {

    private final JugadorEnEquipoRepository jugadorEnEquipoRepository;

    @Autowired
    public JugadorEnEquipoFacade(JugadorEnEquipoRepository jugadorEnEquipoRepository) {
        this.jugadorEnEquipoRepository = jugadorEnEquipoRepository;
    }

    public List<JugadorEnEquipoModel> findAll() {

        List<JugadorEnEquipo> jugadores = jugadorEnEquipoRepository.findAll();

        return JugadorEnEquipoMapper.INSTANCE.toModel(jugadores);
    }

    public JugadorEnEquipoModel findById(Long id) {
        return JugadorEnEquipoMapper.INSTANCE.toModel(
                jugadorEnEquipoRepository.findById(id).orElse(null)
        );
    }

    @Transactional
    public JugadorEnEquipoModel create(JugadorEnEquipoModel jugadorEnEquipo) {
    	
    	JugadorEnEquipo jugador = null;
    	
    	jugador = jugadorEnEquipoRepository.findByJugador_IdAndTorneo_IdAndActivo(jugadorEnEquipo.getJugador(),jugadorEnEquipo.getTorneo(),jugadorEnEquipo.isActivo());
    	
    	if (jugador!=null) {
    	    throw new RuntimeException("Jugador se encuentra activo en este Torneo");
    	}
  
    	
        return JugadorEnEquipoMapper.INSTANCE.toModel(
                jugadorEnEquipoRepository.save(
                        JugadorEnEquipoMapper.INSTANCE.toEntity(jugadorEnEquipo)
                )
        );
    }

    @Transactional
    public JugadorEnEquipoModel update(Long id, JugadorEnEquipoModel jugadorEnEquipo) {
        if (jugadorEnEquipoRepository.existsById(id)) {

            JugadorEnEquipo entity = JugadorEnEquipoMapper.INSTANCE.toEntity(jugadorEnEquipo);
            entity.setId(id);

            return JugadorEnEquipoMapper.INSTANCE.toModel(
                    jugadorEnEquipoRepository.save(entity)
            );
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (jugadorEnEquipoRepository.existsById(id)) {
            jugadorEnEquipoRepository.deleteById(id);
        }
    }
}