package com.sistema.torneos.app.service.impl;

import com.sistema.torneos.app.domain.entity.EquipoEnTorneo;
import com.sistema.torneos.app.domain.repository.EquipoEnTorneoRepository;
import com.sistema.torneos.app.service.EquipoEnTorneoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipoEnTorneoServiceImpl implements EquipoEnTorneoService {

    private final EquipoEnTorneoRepository repository;

    public EquipoEnTorneoServiceImpl(EquipoEnTorneoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipoEnTorneo> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public EquipoEnTorneo findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public EquipoEnTorneo create(EquipoEnTorneo equipoEnTorneo) {
        return repository.save(equipoEnTorneo);
    }

    @Override
    @Transactional
    public EquipoEnTorneo update(Long id, EquipoEnTorneo equipoEnTorneo) {
        EquipoEnTorneo existing = findById(id);
        if (existing != null) {
            existing.setGrupo(equipoEnTorneo.getGrupo());
            return repository.save(existing);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
