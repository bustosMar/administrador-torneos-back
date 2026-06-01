package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.Equipo;
import com.sistema.torneos.app.domain.repository.EquipoRepository;
import com.sistema.torneos.app.service.EquipoService;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipoServiceImpl implements EquipoService {

   private EquipoRepository repository;
    
    
    public EquipoServiceImpl(EquipoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> findAll() {
        return (List<Equipo>) this.repository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Equipo findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Equipo create(Equipo equipo) {
        return this.repository.save(equipo);
    }

    @Override
    @Transactional
    public Equipo update(Long id, Equipo equipo) {
        Equipo existingEquipo = this.findById(id);
        if (existingEquipo != null) {
            // Update the properties of existingEquipo with those from equipo
            return this.repository.save(existingEquipo);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }

    
}
