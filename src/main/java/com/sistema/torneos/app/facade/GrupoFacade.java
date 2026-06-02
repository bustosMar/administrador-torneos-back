package com.sistema.torneos.app.facade;

import com.sistema.torneos.app.domain.entity.Grupo;
import com.sistema.torneos.app.domain.repository.GrupoRepository;

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

    public List<Grupo> findAll() {
        return grupoRepository.findAll();
    }

    public Grupo findById(Long id) {
        return grupoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Grupo create(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    @Transactional
    public Grupo update(Long id, Grupo grupo) {
        if (grupoRepository.existsById(id)) {
            grupo.setId(id);
            return grupoRepository.save(grupo);
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
