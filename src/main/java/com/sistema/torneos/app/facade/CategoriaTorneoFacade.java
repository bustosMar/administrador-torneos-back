package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.CategoriaTorneo;
import com.sistema.torneos.app.domain.repository.CategoriaTorneoRepository;
import com.sistema.torneos.app.web.model.CategoriaTorneoModel;
import com.sistema.torneos.app.web.model.mapper.CategoriaTorneoMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class CategoriaTorneoFacade {

    private final CategoriaTorneoRepository categoriaTorneoRepository;

    @Autowired
    public CategoriaTorneoFacade(CategoriaTorneoRepository categoriaTorneoRepository) {
        this.categoriaTorneoRepository = categoriaTorneoRepository;
    }

    public List<CategoriaTorneoModel> findAll() {
        List<CategoriaTorneo> categoriasTorneo = categoriaTorneoRepository.findAllByTorneoActivo();
        return CategoriaTorneoMapper.INSTANCE.toModel(categoriasTorneo);
    }
  

    public CategoriaTorneoModel findById(Long id) {
        return CategoriaTorneoMapper.INSTANCE.toModel(
                categoriaTorneoRepository.findById(id).orElse(null)
        );
    }

    @Transactional
    public CategoriaTorneoModel create(CategoriaTorneoModel categoriaTorneoModel) {
        CategoriaTorneo categoriaTorneo = CategoriaTorneoMapper.INSTANCE.toEntity(categoriaTorneoModel);
        categoriaTorneo.setActiva(true);
        return CategoriaTorneoMapper.INSTANCE.toModel(categoriaTorneoRepository.save(categoriaTorneo));
    }

    @Transactional
    public CategoriaTorneoModel update(Long id, CategoriaTorneoModel categoriaTorneoModel) {
        if (categoriaTorneoRepository.existsById(id)) {
            CategoriaTorneo entity = CategoriaTorneoMapper.INSTANCE.toEntity(categoriaTorneoModel);
            entity.setId(id);
            return CategoriaTorneoMapper.INSTANCE.toModel(categoriaTorneoRepository.save(entity));
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        if (categoriaTorneoRepository.existsById(id)) {
            categoriaTorneoRepository.deleteById(id);
        }
    }
}
