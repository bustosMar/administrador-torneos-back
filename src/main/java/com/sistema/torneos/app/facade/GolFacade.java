package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Gol;
import com.sistema.torneos.app.domain.entity.Grupo;
import com.sistema.torneos.app.domain.repository.GolRepository;
import com.sistema.torneos.app.web.model.GolModel;
import com.sistema.torneos.app.web.model.mapper.GolMapper;
import com.sistema.torneos.app.web.model.mapper.GrupoMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class GolFacade {

    private final GolRepository golRepository;

    @Autowired
    public GolFacade(GolRepository golRepository) {
        this.golRepository = golRepository;
    }

    public List<GolModel> findAll() {
    	
        List<Gol> goles = golRepository.findAll();

        return GolMapper.INSTANCE.toModel(goles);
        
    }

    public GolModel findById(Long id) {
    	
        return GolMapper.INSTANCE.toModel(golRepository.findById(id).orElse(null));
    }

    @Transactional
    public GolModel create(GolModel gol) {
        return GolMapper.INSTANCE.toModel(golRepository.save(GolMapper.INSTANCE.toEntity(gol)));

    }

    @Transactional
    public GolModel update(Long id, GolModel golModel) {
        if (golRepository.existsById(id)) {
        	Gol gol = GolMapper.INSTANCE.toEntity(golModel);
        	gol.setId(id);
        	
            return GolMapper.INSTANCE.toModel(golRepository.save(gol));
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (golRepository.existsById(id)) {
            golRepository.deleteById(id);
        }
    }
}
