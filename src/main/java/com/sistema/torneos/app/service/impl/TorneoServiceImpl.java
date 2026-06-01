package com.sistema.torneos.app.service.impl;


import com.sistema.torneos.app.domain.entity.Torneo;
import com.sistema.torneos.app.domain.repository.TorneoRepository;

import org.springframework.transaction.annotation.Transactional;
import com.sistema.torneos.app.service.TorneoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TorneoServiceImpl implements TorneoService {

    private TorneoRepository repository;
    
    
    public TorneoServiceImpl(TorneoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Torneo> findAll() {
        return (List<Torneo>) this.repository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Torneo findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Torneo create(Torneo torneo) {
        return this.repository.save(torneo);
    }

    @Override
    @Transactional
    public Torneo update(Long id, Torneo torneo) {
        Torneo existingTorneo = this.findById(id);
        if (existingTorneo != null) {
            // Update the properties of existingTorneo with those from torneo
            return this.repository.save(existingTorneo);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}
