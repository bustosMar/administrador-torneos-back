package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Grupo;
import com.sistema.torneos.app.domain.repository.GrupoRepository;
import com.sistema.torneos.app.web.model.GrupoModel;
import com.sistema.torneos.app.web.model.mapper.GrupoMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class GrupoFacade {

    private final GrupoRepository grupoRepository;

    @Autowired
    public GrupoFacade(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public List<GrupoModel> findAll() {

        List<Grupo> grupos = grupoRepository.findAll();

        return GrupoMapper.INSTANCE.toModel(grupos);
    }

    public GrupoModel findById(Long id) {
        
        return GrupoMapper.INSTANCE.toModel(grupoRepository.findById(id).orElse(null));
    }

    @Transactional
    public GrupoModel create(GrupoModel grupo) {

        return GrupoMapper.INSTANCE.toModel(grupoRepository.save(GrupoMapper.INSTANCE.toEntity(grupo)));

    }

    @Transactional
    public GrupoModel update(Long id, GrupoModel grupoModel) {

        if (grupoRepository.existsById(id)) {
            Grupo grupo = GrupoMapper.INSTANCE.toEntity(grupoModel);
            grupo.setId(id);
            return GrupoMapper.INSTANCE.toModel(grupoRepository.save(grupo));
        }

        return null;
    }

    @Transactional
    public void delete(Long id) {

        if (grupoRepository.existsById(id)) {
            grupoRepository.deleteById(id);
        }
        
    }
}
