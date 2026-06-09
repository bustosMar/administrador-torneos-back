package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.domain.repository.TorneoRepository;
import com.sistema.torneos.app.web.model.TorneoModel;
import com.sistema.torneos.app.web.model.mapper.TorneoMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class TorneoFacade {

    private final TorneoRepository torneoRepository;

    @Autowired
    public TorneoFacade(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    public List<TorneoModel> findAll() {
    	List<Torneo> torneos = torneoRepository.findAll();
        return TorneoMapper.INSTANCE.toModel(torneos);
    }

    public TorneoModel findById(Long id) {
    	
    	return TorneoMapper.INSTANCE.toModel(torneoRepository.findById(id).orElse(null));
        
    }

    @Transactional
    public TorneoModel create(TorneoModel torneoModel) {
    	
    	Torneo torneo = null;
    	
    	torneo = torneoRepository
    	        .findByNombre(torneoModel.getNombre());
    	
    	if (torneo!=null) {
    	    throw new RuntimeException("Ya existe un torneo con ese nombre");
    	}
    	
    	return TorneoMapper.INSTANCE.toModel(torneoRepository.save(TorneoMapper.INSTANCE.toEntity(torneoModel)));
        
    }

    @Transactional
    public TorneoModel update(Long id, TorneoModel torneoModel) {
    	
        if (torneoRepository.existsById(id)) {
        	
        	Torneo torneo = TorneoMapper.INSTANCE.toEntity(torneoModel);
            torneo.setId(id);
            return TorneoMapper.INSTANCE.toModel(torneoRepository.save(torneo));
        }
        
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (torneoRepository.existsById(id)) {
            torneoRepository.deleteById(id);
        }
    }
}
