package com.sistema.torneos.app.service.impl;


import com.sistema.torneos.app.domain.entity.Partido;
import com.sistema.torneos.app.domain.repository.PartidoRepository;

import org.springframework.transaction.annotation.Transactional;
import com.sistema.torneos.app.service.PartidoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidoServiceImpl implements PartidoService {

    private PartidoRepository repository;
    
    
    public PartidoServiceImpl(PartidoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partido> findAll() {
        return (List<Partido>) this.repository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Partido findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Partido create(Partido partido) {
        return this.repository.save(partido);
    }

    @Override
    @Transactional
    public Partido update(Long id, Partido partido) {
        Partido existingPartido = this.findById(id);
        if (existingPartido != null) {
            // Update the properties of existingPartido with those from partido
            return this.repository.save(existingPartido);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }

}
