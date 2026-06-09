package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Arbitro;
import com.sistema.torneos.app.domain.repository.ArbitroRepository;
import com.sistema.torneos.app.web.model.ArbitroModel;
import com.sistema.torneos.app.web.model.mapper.ArbitroMapper;
import com.sistema.torneos.app.web.model.mapper.GrupoMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class ArbitroFacade {

    private final ArbitroRepository arbitroRepository;

    @Autowired
    public ArbitroFacade(ArbitroRepository arbitroRepository) {
        this.arbitroRepository = arbitroRepository;
    }

    public List<ArbitroModel> findAll() {
    	
    	List<Arbitro> arbitros = arbitroRepository.findAll();
    	
    	 return ArbitroMapper.INSTANCE.toModel(arbitros);
    }

    public ArbitroModel findById(Long id) {
    	 return ArbitroMapper.INSTANCE.toModel(arbitroRepository.findById(id).orElse(null));
    }

    @Transactional
    public ArbitroModel create(ArbitroModel arbitroModel) {
    	
    	Arbitro arbitro = null;
    	
    	arbitro = arbitroRepository.findByNombreAndApellido(arbitroModel.getNombre(),arbitroModel.getApellido());
    	
    	if (arbitro!=null) {
    	    throw new RuntimeException("Ya existe un arbitro con estos datos");
    	}
    	
    	return ArbitroMapper.INSTANCE.toModel(arbitroRepository.save(ArbitroMapper.INSTANCE.toEntity(arbitroModel)));
    }

    @Transactional
    public ArbitroModel update(Long id, ArbitroModel arbitroModel) {
        if (arbitroRepository.existsById(id)) {
            arbitroModel.setId(id);
            return ArbitroMapper.INSTANCE.toModel(arbitroRepository.save(ArbitroMapper.INSTANCE.toEntity(arbitroModel)));
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (arbitroRepository.existsById(id)) {
            arbitroRepository.deleteById(id);
        }
    }
}
