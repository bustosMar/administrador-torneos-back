package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.Grupo;
import com.sistema.torneos.app.domain.repository.GrupoRepository;
import com.sistema.torneos.app.service.GrupoService;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GrupoServiceImpl implements GrupoService {

   private GrupoRepository repository;
    
    
    public GrupoServiceImpl(GrupoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grupo> findAll() {
        return (List<Grupo>) this.repository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Grupo findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Grupo create(Grupo grupo) {
        return this.repository.save(grupo);
    }

    @Override
    @Transactional
    public Grupo update(Long id, Grupo grupo) {
        Grupo existingGrupo = this.findById(id);
        if (existingGrupo != null) {
            // Update the properties of existingGrupo with those from grupo
            return this.repository.save(existingGrupo);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }

    
}
