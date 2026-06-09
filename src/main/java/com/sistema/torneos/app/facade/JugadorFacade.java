package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Jugador;
import com.sistema.torneos.app.domain.repository.JugadorRepository;
import com.sistema.torneos.app.web.model.JugadorModel;
import com.sistema.torneos.app.web.model.mapper.JugadorMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class JugadorFacade {

    private final JugadorRepository jugadorRepository;

    @Autowired
    public JugadorFacade(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public List<JugadorModel> findAll() {
    	
    	 List<Jugador> grupos = jugadorRepository.findAll();

         return JugadorMapper.INSTANCE.toModel(grupos);
    }

    public JugadorModel findById(Long id) {
        return JugadorMapper.INSTANCE.toModel(jugadorRepository.findById(id).orElse(null));
    }

    @Transactional
    public JugadorModel create(JugadorModel jugadorModel) {
    	
    	Jugador jugador = null;
    	
    	jugador = jugadorRepository.findByNombreAndApellidoAndFechaNacimiento(jugadorModel.getNombre(), jugadorModel.getApellido(), jugadorModel.getFechaNacimiento());
    	
    	if (jugador!=null) {
       	    throw new RuntimeException("Ya existe un jugador con esos datos");
       	}
    			
    			
    	return JugadorMapper.INSTANCE.toModel(jugadorRepository.save(JugadorMapper.INSTANCE.toEntity(jugadorModel)));
        
    }


    @Transactional
    public JugadorModel update(Long id, JugadorModel jugadorModel) {
        if (jugadorRepository.existsById(id)) {
        	 Jugador jugador = JugadorMapper.INSTANCE.toEntity(jugadorModel);
        	 jugador.setId(id);
             return JugadorMapper.INSTANCE.toModel(jugadorRepository.save(jugador));
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (jugadorRepository.existsById(id)) {
            jugadorRepository.deleteById(id);
        }
    }
}
